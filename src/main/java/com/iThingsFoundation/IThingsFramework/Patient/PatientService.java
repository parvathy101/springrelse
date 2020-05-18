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
		String customerId=null;
		String physicianId=null;
		String lastpart=null;
       String exist=null;
		
		
		if(patien.getPhysicianId()!=null)
		{
			 exist=getPhysicianExist(patien.getPhysicianId().toString());
		}
		else if(patien.getCustomerId()!=null)
		{
			exist=getCustomerExist(patien.getCustomerId().toString());
		}
		if(exist=="yes")
		{
		
		if(patien.getCustomerId()!=null)
		{
			tenantId=getCustomerTenantId(patien.getCustomerId().toString());
			customerId=patien.getCustomerId().toString();
			physicianId=null;
			lastpart="Status='active' and TenantId='"+tenantId+"' and CustomerUUId='"+customerId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
		}
		else if(patien.getPhysicianId()!=null)		
		{
			tenantId=getPhysicianTenantId(patien.getPhysicianId().toString());
			customerId=getPhysicianCustomerId(patien.getPhysicianId().toString());
			physicianId=patien.getPhysicianId().toString();
			lastpart="Status='active' and TenantId='"+tenantId+"' and CustomerUUId='"+customerId+"' and UserId='"+physicianId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
			
		}
		
		String sql="select t.* from (select @rownum:=@rownum+1 rownumber, t.* from Patient t cross join (SELECT @rownum:=0) r where ";
		
		
		String sql2="SELECT count(Id) FROM Patient where ";
		 if(patien.getFilter()!=null)
		 {
			 //String sql3;
			      if(patien.getFilter().getFirstName()!=null)
			      {
			    	  sql+= "Name like '"+patien.getFilter().getFirstName()+"%' and "; 
			    	  sql2+= "Name like '"+patien.getFilter().getFirstName()+"%' and ";
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
			    	  sql+= "DOB like '"+patien.getFilter().getDob()+"%' and "; 
			    	  sql2+= "DOB like '"+patien.getFilter().getDob()+"%' and "; 
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
			 patient.setFirstName((String)row.get("Name"));
			 patient.setEmail((String)row.get("Email"));
			 String dob=dateFormat.format(row.get("DOB")).toString();
			 patient.setDob(dob);
			 Address address=new Address();
			  address.setStreet((String)row.get("Street"));
			  address.setStreetAdditional((String)row.get("Street2"));
			  address.setPostalcode((String)row.get("PostalCode"));
			  address.setCity((String)row.get("City"));
			  address.setCountry((String)row.get("Country"));
			  patient.setAddress(address);
			 patient.setGender((String)row.get("Gender"));
			  double val=(double)row.get("rownumber");
			  int rowno=(int) Math.round(val);
			   lastRowno=Integer.toString(rowno);
			   
			   
			   id=(int) row.get("Id");
			      
			   patients.add(patient);
	        }
		 
		     String sqlnew = null;
		     if(patien.getCustomerId()!=null)
				{
					tenantId=getCustomerTenantId(patien.getCustomerId().toString());
					customerId=patien.getCustomerId().toString();
					physicianId=null;
					sqlnew = sql2+" Id>"+id+" and CustomerUUId='"+customerId+"' and TenantId='"+tenantId+"' ";
				}
				else if(patien.getPhysicianId()!=null)		
				{
					tenantId=getPhysicianTenantId(patien.getPhysicianId().toString());
					customerId=getPhysicianCustomerId(patien.getPhysicianId().toString());
					physicianId=patien.getPhysicianId().toString();
					sqlnew = sql2+" Id>"+id+" and CustomerUUId='"+customerId+"' and TenantId='"+tenantId+"' and UserId='"+physicianId+"'";
					
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
		String customerId=null;
		if(patient.getCustomerId()!=null)
		{
			tenantId=getCustomerTenantId(patient.getCustomerId().toString());
			customerId=patient.getCustomerId().toString();
		}
		else if(patient.getPhysicianId()!=null)		
		{
			tenantId=getPhysicianTenantId(patient.getPhysicianId().toString());
			customerId=getPhysicianCustomerId(patient.getPhysicianId().toString());
			
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
		
			String profilesql="Insert into Profile(PatientId,CustomerUUId,UserId,TenantId,RoleId,Name,Email,Phone,Created_On,Street,Street2,PostalCode,City,Country) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertprofile=jdbcTemplate.update(profilesql,uuid.toString(),customerId,patient.getPhysicianId(),tenantId,8,patient.getFirstName(),patient.getEmail(),patient.getPhone(),timestamp.getTime(),patient.getAddress().getStreet(),patient.getAddress().getStreetAdditional(),patient.getAddress().getPostalcode(),patient.getAddress().getCity(),patient.getAddress().getCountry());
			
			String patientsql="Insert into Patient(PatientId,CustomerUUId,UserId,TenantId,Name,Email,Password,Status,Created_On,Gender,DOB,Street,Street2,PostalCode,City,Country) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertpatient=jdbcTemplate.update(patientsql,uuid.toString(),customerId,patient.getPhysicianId(),tenantId,patient.getFirstName(),patient.getEmail(),randompassword,"active",timestamp.getTime(),patient.getGender(),patient.getDob(),patient.getAddress().getStreet(),patient.getAddress().getStreetAdditional(),patient.getAddress().getPostalcode(),patient.getAddress().getCity(),patient.getAddress().getCountry());
			
			
			String loginsql="Insert into Login1(PatientId,CustomerUUId,UserId,TenantId,UserName,Password,RoleId) values (?,?,?,?,?,?,?)";
			int insertlogin=jdbcTemplate.update(loginsql,uuid.toString(),customerId,patient.getPhysicianId(),tenantId,patient.getEmail(),randompassword,8);
			
			if(insertprofile>0&&insertpatient>0&&insertlogin>0)
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
		
		String profilesql="Update Profile set Name=?,Phone=?,Modified_On=?,Street=?,Street2=?,PostalCode=?,City=?,Country=? where PatientId=?";
		int updateProfile=jdbcTemplate.update(profilesql,patient.getFirstName(),patient.getPhone(),timestamp.getTime(),patient.getAddress().getStreet(),patient.getAddress().getStreetAdditional(),patient.getAddress().getPostalcode(),patient.getAddress().getCity(),patient.getAddress().getCountry(),patient.getPatientuuid());
		
		String patientsql="Update Patient set Name=?,Gender=?,DOB=?,Modified_On=?,Street=?,Street2=?,PostalCode=?,City=?,Country=? where PatientId=?";
		int updatepatient=jdbcTemplate.update(patientsql,patient.getFirstName(),patient.getGender(),patient.getDob(),timestamp.getTime(),patient.getAddress().getStreet(),patient.getAddress().getStreetAdditional(),patient.getAddress().getPostalcode(),patient.getAddress().getCity(),patient.getAddress().getCountry(),patient.getPatientuuid());
		if(updateProfile>0&&updatepatient>0)
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
		
		String loginsql="Delete FROM Login1 WHERE PatientId=?";
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
	
	public String getCustomerTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Customer WHERE CustomerUUId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPhysicianTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM User WHERE UserId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	public String getPhysicianCustomerId(String id)
	{
		
		String sql = "SELECT CustomerUUId FROM User WHERE UserId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getCustomerExist(String id)
	{
		
		String sql = "SELECT * FROM Patient WHERE CustomerUUId=? and Status='active' ";
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
		
		String sql = "SELECT * FROM Patient WHERE UserId=? and Status='active' ";
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
	

}
