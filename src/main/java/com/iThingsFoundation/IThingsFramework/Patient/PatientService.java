package com.iThingsFoundation.IThingsFramework.Patient;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.uuid.Generators;
import com.iThingsFoundation.IThingsFramework.Physician.Physician;
import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.Filters;
import com.iThingsFoundation.IThingsFramework.common.Message;

@Service
public class PatientService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	UUID uuid;
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	
	
	public PatientList getPatientDetails(String batchsize,String offset,Patient patien)
	{
		ArrayList<Patient> patients=new ArrayList<>();
		PatientList list=new PatientList();
		Filters fil=new Filters() ;
		String remainingRecords=null;
		String lastRowno=null;
		int id=0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String tenantId=null;
		String companyId=null;
		String physicianId=null;
		String rootId=null;
		String lastpart=null;
       String exist=null;
		
		
		if(patien.getPhysicianId()!=null)
		{
			 exist=getPhysicianExist(patien.getPhysicianId().toString());
		}
		else if(patien.getCompanyId()!=null)
		{
			exist=getCustomerExist(patien.getCompanyId().toString());
		}
		if(exist=="yes")
		{
		
		if(patien.getCompanyId()!=null)
		{
			tenantId=getCompanyTenantId(patien.getCompanyId().toString());
			rootId=getTenantRootId(tenantId);
			companyId=patien.getCompanyId().toString();
			physicianId=null;
			lastpart="Status='active' and TenantId='"+tenantId+"' and CompanyId='"+companyId+"'and RoleId=8 order by ProfileId asc ) t limit "+offset+","+batchsize+"";
		}
		else if(patien.getPhysicianId()!=null)		
		{
			tenantId=getPhysicianTenantId(patien.getPhysicianId().toString());
			rootId=getTenantRootId(tenantId);
			companyId=getPhysicianCompanyId(patien.getPhysicianId().toString());
			physicianId=patien.getPhysicianId().toString();
			lastpart="Status='active' and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' and PhysicianId='"+physicianId+"'and RoleId=8 order by ProfileId asc ) t limit "+offset+","+batchsize+"";
			
		}
		
		String sql="select t.* from (select @rownum:=@rownum+1 rownumber, t.* from Profile t cross join (SELECT @rownum:=0) r where ";
		
		
		String sql2="SELECT count(ProfileId) FROM Profile where ";
		 if(patien.getFilter()!=null)
		 {
			 //String sql3;
			      if(patien.getFilter().getFirstName()!=null)
			      {
			    	  sql+= "FirstName like '"+patien.getFilter().getFirstName()+"%' and "; 
			    	  sql2+= "FirstName like '"+patien.getFilter().getFirstName()+"%' and ";
			      }
			      if(patien.getFilter().getEmail()!=null)
			      {
			    	  sql+= "Email like '"+patien.getFilter().getEmail()+"%' and "; 
			    	  sql2+= "Email like '"+patien.getFilter().getEmail()+"%' and "; 
			    	  
			      }
			      if(patien.getFilter().getPhone()!=null)
			      {
			    	  sql+= "Phone like '"+patien.getFilter().getPhone()+"%' and "; 
			    	  sql2+= "Phone like '"+patien.getFilter().getPhone()+"%' and "; 
			      }
			      if(patien.getFilter().getGender()!=null)
			      {
			    	  sql+= "Gender like '"+patien.getFilter().getGender()+"%' and "; 
			    	  sql2+= "Gender like '"+patien.getFilter().getGender()+"%' and "; 
			      }
			      if(patien.getFilter().getDob()!=null)
			      {
			    	  sql+= "Dob like '"+patien.getFilter().getDob()+"%' and "; 
			    	  sql2+= "Dob like '"+patien.getFilter().getDob()+"%' and "; 
			      }
			     
			      
			
			
			sql+=lastpart;
			
		
		 }
		 else
		 {
			 sql+=lastpart;
			 
		 }
		
	    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		
		//String count=jdbcTemplate.queryForObject("select * from Customer");
		 for (Map<String, Object> row : rows) 
	        {
			 Patient patient=new Patient();
			 patient.setPatientuuid(row.get("PatientId"));
			 String pId=(String)row.get("PatientId");
			 patient.setFirstName((String)row.get("FirstName"));
			 patient.setLastName((String)row.get("LastName"));
			 patient.setEmail((String)row.get("Email"));
			 String dob=dateFormat.format(row.get("DOB")).toString();
			 patient.setDob(dob);
			 Address address=new Address();
			 address=getPatientAddress(pId);
			 // address.setStreet((String)row.get("Street"));
			 // address.setStreetAdditional((String)row.get("StreetAdditional"));
			 // address.setPostalcode((String)row.get("PostalCode"));
			 // address.setCity((String)row.get("City"));
			 // address.setState((String)row.get("State"));
			 // address.setCountry((String)row.get("Country"));
			  patient.setAddress(address);
			 patient.setGender((String)row.get("Gender"));
			  double val=(double)row.get("rownumber");
			  int rowno=(int) Math.round(val);
			   lastRowno=Integer.toString(rowno);
			   
			   
			   id=(int) row.get("ProfileId");
			      
			   patients.add(patient);
	        }
		 
		     String sqlnew = null;
		     if(patien.getCompanyId()!=null)
				{
					tenantId=getCompanyTenantId(patien.getCompanyId().toString());
					companyId=patien.getCompanyId().toString();
					physicianId=null;
					sqlnew = sql2+" ProfileId>"+id+" and CompanyId='"+companyId+"' and TenantId='"+tenantId+"'and RoleId=8 and Status='active' ";
				}
				else if(patien.getPhysicianId()!=null)		
				{
					tenantId=getPhysicianTenantId(patien.getPhysicianId().toString());
					companyId=getPhysicianCompanyId(patien.getPhysicianId().toString());
					physicianId=patien.getPhysicianId().toString();
					sqlnew = sql2+" ProfileId>"+id+" and CompanyId='"+companyId+"' and TenantId='"+tenantId+"' and PhysicianId='"+physicianId+"' and RoleId=8 and Status='active'";
					
				}
		     remainingRecords = (String) jdbcTemplate.queryForObject(sqlnew, String.class);
		     
		       list.setNextoffset(lastRowno);
		       list.setRemainingRecords(remainingRecords);
		      list.setPatient(patients);
		      list.setStatus("Success");
		}
		else
		{
			list.setStatus("Not Found");
		}
		    
		return list;
	}
	
	
	public Patient register(Patient patient)
	{
		uuid= Generators.timeBasedGenerator().generate();
		Patient registerPatient=new Patient();
		Message msg=new Message();
		Address addr=new Address();
		
		String tenantId = null;
		String companyId=null;
		String rootId=null;
		String physicianId=null;
		
		if(patient.getCompanyId()!=null)
		{
			tenantId=getCompanyTenantId(patient.getCompanyId().toString());
			rootId=getTenantRootId(tenantId);
			companyId=patient.getCompanyId().toString();
		}
		else if(patient.getPhysicianId()!=null)		
		{
			
			companyId=getPhysicianCompanyId(patient.getPhysicianId().toString());
			tenantId=getPhysicianTenantId(patient.getPhysicianId().toString());
			rootId=getTenantRootId(tenantId);
			physicianId=patient.getPhysicianId().toString();
			
		}
		String sql="select * from Profile where Email=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,patient.getEmail());
		if(rows.size()>0)
		{
		
			msg.setErrorMessage("Already registered");
			registerPatient.setMessage(msg);
		return registerPatient;	
		}
		else
		{
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			 String randompassword= RandomStringUtils.random(10,characters);
				
				/*
				//change loginpasswrd to generated String line no 67
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				
				String generatedString  = passwordEncoder.encode(randompassword);
				System.out.println(randompassword+"----aaaaa---"+generatedString);
			    */
		
			
			
			String profilesql="Insert into Profile(RootId,PatientId,PhysicianId,CompanyId,TenantId,RoleId,FirstName,LastName,Email,Phone,Gender,Dob,Created_On,Street,StreetAdditional,PostalCode,City,State,Country,Status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertprofile=jdbcTemplate.update(profilesql,rootId,uuid.toString(),physicianId,companyId,tenantId,8,patient.getFirstName(),patient.getLastName(),patient.getEmail(),patient.getPhone(),patient.getGender(),patient.getDob(),timestamp.getTime(),patient.getAddress().getStreet(),patient.getAddress().getStreetAdditional(),patient.getAddress().getPostalcode(),patient.getAddress().getCity(),patient.getAddress().getState(),patient.getAddress().getCountry(),"active");
			
						
						
			String patientsql="Insert into Patient(PatientId,PhysicianId,CompanyId,TenantId,RootId,Status,Created_On) values (?,?,?,?,?,?,?)";
			int insertpatient=jdbcTemplate.update(patientsql,uuid.toString(),physicianId,companyId,tenantId,rootId,"active",timestamp.getTime());
			
			
			String loginsql="Insert into Login(UserName,Password,UUID,RoleId) values (?,?,?,?)";
			int insertlogin=jdbcTemplate.update(loginsql,patient.getEmail(),randompassword,uuid.toString(),8);
			
			String addresssql="Insert into Address(UUID,RoleId,Street,StreetAdditional,PostalCode,City,State,Country) values(?,?,?,?,?,?,?,?)";
			int insertaddress=jdbcTemplate.update(addresssql,uuid.toString(),8,patient.getAddress().getStreet(),patient.getAddress().getStreetAdditional(),patient.getAddress().getPostalcode(),patient.getAddress().getCity(),patient.getAddress().getState(),patient.getAddress().getCountry());
			
			if(insertprofile>0&&insertpatient>0&&insertlogin>0&&insertaddress>0)
			{
				msg.setSuccessMessage("Registered Successfully ");
				registerPatient.setMessage(msg);
				registerPatient.setPatientuuid(uuid);
			
			}
			
			
			
			
			
		return registerPatient;
		}
	}
	
	public Message updateById(Patient patient)
	{
		
		Message msg=new Message();
		Address addr=new Address();
		
		String physicianId=getPatientPhysicianId(patient.getPatientuuid().toString());
		String companyId=getPatientCompanyId(patient.getPatientuuid().toString());
		String tenantId=getPatientTenantId(patient.getPatientuuid().toString());
		String rootId=getTenantRootId(tenantId);
		String profilesql=null;
		
		if(physicianId==null)
		{
			profilesql="Update Profile set FirstName=?,LastName=?,Phone=?,Gender=?,Dob=?,Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=?,Modified_On=? where TenantId=? and RootId=? and CompanyId=? and PhysicianId is null and patientId=? ";
	     
		}
		else
		{
			profilesql="Update Profile set FirstName=?,LastName=?,Phone=?,Gender=?,Dob=?,Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=?,Modified_On=? where TenantId=? and RootId=? and CompanyId=? and PhysicianId='"+physicianId+"' and patientId=? ";
		     
		}
		
		
		int updateProfile=jdbcTemplate.update(profilesql,patient.getFirstName(),patient.getLastName(),patient.getPhone(),patient.getGender(),patient.getDob(),patient.getAddress().getStreet(),patient.getAddress().getStreetAdditional(),patient.getAddress().getPostalcode(),patient.getAddress().getCity(),patient.getAddress().getState(),patient.getAddress().getCountry(),timestamp.getTime(),tenantId,rootId,companyId,patient.getPatientuuid());
		
		String patientsql="Update Patient set Modified_On=? where PatientId=?";
		int updatepatient=jdbcTemplate.update(patientsql,timestamp.getTime(),patient.getPatientuuid());
		
		 String addresssql="Update Address set Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=? where UUID=?";
		 int updateaddress=jdbcTemplate.update(addresssql,patient.getAddress().getStreet(),patient.getAddress().getStreetAdditional(),patient.getAddress().getPostalcode(),patient.getAddress().getCity(),patient.getAddress().getState(),patient.getAddress().getCountry(),patient.getPatientuuid());
		 
		
		if(updateProfile>0&&updatepatient>0&&updateaddress>0)
		{
			msg.setSuccessMessage("Update Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Update Details");
		}
		return msg;
	}
	
	public Message deleteById(Patient patient)
	{
		Message msg=new Message();
		int deleteProfile=0;
		String physicianId=getPatientPhysicianId(patient.getPatientuuid().toString());
		String companyId=getPatientCompanyId(patient.getPatientuuid().toString());
		String tenantId=getPatientTenantId(patient.getPatientuuid().toString());
		String rootId=getTenantRootId(tenantId);
		
		if(physicianId==null)
		{
			deleteProfile=jdbcTemplate.update("Update Profile set Status='inactive' where TenantId=? and RootId=? and CompanyId=? and PhysicianId is null and patientId=? ",tenantId,rootId,companyId,patient.getPatientuuid());
	     
		}
		else
		{
			deleteProfile=jdbcTemplate.update("Update Profile set Status='inactive' where TenantId=? and RootId=? and CompanyId=? and PhysicianId=? and patientId=? ",tenantId,rootId,companyId,physicianId,patient.getPatientuuid());
		     
		}
		
		
		
		String loginsql="Delete FROM Login WHERE UUID=?";
		int deleteLogin=jdbcTemplate.update(loginsql,patient.getPatientuuid());
		
		String patientsql="Update Patient set Status='inactive' where PatientId=?";
		int deletepatient=jdbcTemplate.update(patientsql,patient.getPatientuuid());
		
		
		if(deletepatient>0)
		{
			msg.setSuccessMessage("Delete Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Delete Details");
		}
		return msg;
	}
	
	public String getCompanyTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Company WHERE CompanyId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPhysicianTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Physician WHERE PhysicianId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	public String getPhysicianCompanyId(String id)
	{
		
		String sql = "SELECT CompanyId FROM Physician WHERE PhysicianId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getCustomerExist(String id)
	{
		
		String sql = "SELECT * FROM Patient WHERE CompanyId=? and Status='active' ";
        String exist=null;
       List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,id);
		
		if(rows.size()>0)
		{
		exist="yes";
		}
		else
		{
			exist="no";
        }

	    return exist;
		
	}
	
	public String getPhysicianExist(String id)
	{
		
		String sql = "SELECT * FROM Patient WHERE PhysicianId=? and Status='active' ";
        String exist=null;
       List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,id);
		
		if(rows.size()>0)
		{
		exist="yes";
		}
		else
		{
			exist="no";
        }

	    return exist;
		
	}
	public String getTenantRootId(String id)
	{
		
		String sql = "SELECT RootId FROM Tenant WHERE TenantId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPatientCompanyId(String id)
	{
		
		String sql = "SELECT CompanyId FROM Patient WHERE PatientId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPatientPhysicianId(String id)
	{
		
		String sql = "SELECT PhysicianId FROM Patient WHERE PatientId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPatientTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Patient WHERE PatientId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public Address getPatientAddress(String Id)
	{
		  Address address=new Address();
		  List<Map<String, Object>> rows = jdbcTemplate.queryForList("Select * from Address where UUID='"+Id+"'");
		   
			//String count=jdbcTemplate.queryForObject("select * from Customer");
			 for (Map<String, Object> row : rows) 
		        {
		  address.setStreet((String)row.get("Street"));
		  address.setStreetAdditional((String)row.get("StreetAdditional"));
		  address.setPostalcode((String)row.get("PostalCode"));
		  address.setState((String)row.get("State"));
		  address.setCity((String)row.get("City"));
		  address.setCountry((String)row.get("Country"));
		        }
			 return address;
	}

}
