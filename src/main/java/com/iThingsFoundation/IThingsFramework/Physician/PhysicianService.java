package com.iThingsFoundation.IThingsFramework.Physician;

import java.sql.Timestamp;
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
import com.iThingsFoundation.IThingsFramework.Company.Company;
import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.EmailService;
import com.iThingsFoundation.IThingsFramework.common.Filters;
import com.iThingsFoundation.IThingsFramework.common.Message;

@Service
public class PhysicianService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private EmailService emailService;
	
	UUID uuid;
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	
	
	public PhysicianList getPhysicianDetails(String batchsize,String offset,Physician phys)
	{
		ArrayList<Physician> physicians=new ArrayList<>();
		PhysicianList list=new PhysicianList();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Filters fil=new Filters() ;
		String remainingRecords=null;
		String lastRowno=null;
		int id=0;
		
		String tenantId=null;
		String companyId=null;
		String lastpart=null;
		String rootId=null;
		String exist=null;
		
		
		if(phys.getTenantId()!=null)
		{
			 exist=getTenantExist(phys.getTenantId().toString());
		}
		else if(phys.getCompanyId()!=null)
		{
			exist=getCompanyExist(phys.getCompanyId().toString());
		}
		if(exist=="yes")
		{
			
			
			if(phys.getTenantId()!=null)
			{
				tenantId=phys.getTenantId().toString();
				rootId=getTenantRootId(tenantId);
				companyId=null;
		        lastpart="Status='active' and TenantId='"+tenantId+"' and RootId='"+rootId+"' and RoleId in(6,7) order by ProfileId asc ) t limit "+offset+","+batchsize+"";
		        
			}
			else if(phys.getCompanyId()!=null)
			{
				companyId=phys.getCompanyId().toString();
				tenantId=getTenantId(companyId);
				rootId=getTenantRootId(tenantId);
			    lastpart="Status='active' and CompanyId='"+companyId+"' and TenantId='"+tenantId+"'and RootId='"+rootId+"' and RoleId in(6,7) order by ProfileId asc ) t limit "+offset+","+batchsize+"";
			    
			}
			
		String sql="select t.* from (select @rownum:=@rownum+1 rownumber, t.* from Profile t cross join (SELECT @rownum:=0) r where ";
		
		//String lastpart="Status='active' and CustomerUUId='"+customerId+"' and TenantId='"+tenantId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
		String sql2="SELECT count(ProfileId) FROM Profile where ";
		 if(phys.getFilter()!=null)
		 {
			 //String sql3;
			      if(phys.getFilter().getFirstName()!=null)
			      {
			    	  sql+= "FirstName like '"+phys.getFilter().getFirstName()+"%' and "; 
			    	  sql2+= "FirstName like '"+phys.getFilter().getFirstName()+"%' and ";
			      }
			      if(phys.getFilter().getEmail()!=null)
			      {
			    	  sql+= "Email like '"+phys.getFilter().getEmail()+"%' and "; 
			    	  sql2+= "Email like '"+phys.getFilter().getEmail()+"%' and "; 
			    	  
			      }
			      if(phys.getFilter().getPhone()!=null)
			      {
			    	  sql+= "Phone like '"+phys.getFilter().getPhone()+"%' and "; 
			    	  sql2+= "Phone like '"+phys.getFilter().getPhone()+"%' and "; 
			      }
			     
			      
			
			
			sql+=lastpart;
			
		
		 }
		 else
		 {
			 sql+=lastpart;
			 
		 }
		 
		 System.out.println(sql);
		   
	    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		
		//String count=jdbcTemplate.queryForObject("select * from Customer");
		 for (Map<String, Object> row : rows) 
	        {
			  Physician physician=new Physician();
			  physician.setPhysicianuuid(row.get("PhysicianId"));
			  String phyId=(String)row.get("PhysicianId");
			  physician.setFirstName((String)row.get("FirstName"));
			  physician.setLastName((String)row.get("LastName"));
			  physician.setPhone((String)row.get("Phone"));
			  physician.setEmail((String)row.get("Email"));
			  physician.setGender((String)row.get("Gender"));
			  String dob=dateFormat.format(row.get("Dob")).toString();
			  physician.setDob(dob);
			  Address address=new Address();
			  address=getPhysicianAddress(phyId);
			  physician.setAddress(address);
			  physician.setRoleId(row.get("RoleId").toString());
			  double val=(double)row.get("rownumber");
			  int rowno=(int) Math.round(val);
			   lastRowno=Integer.toString(rowno);
			  
			   
			   id=(int) row.get("ProfileId");
			      
			   physicians.add(physician);
	        }
		 
		     String sqlnew =null;
             if(phys.getTenantId()!=null)
     		{
     			tenantId=phys.getTenantId().toString();
     			companyId=null;
     			rootId=getTenantRootId(tenantId);
     			 sqlnew = sql2+" ProfileId>"+id+" and TenantId='"+tenantId+"' and RootId='"+rootId+"' and RoleId in(6,7) and Status='active'";
     		}
     		else if(phys.getCompanyId()!=null)
     		{
     			companyId=phys.getCompanyId().toString();
     			tenantId=getTenantId(companyId);
     			rootId=getTenantRootId(tenantId);
     			sqlnew = sql2+" ProfileId>"+id+" and CompanyId='"+companyId+"' and TenantId='"+tenantId+"' and RootId='"+rootId+"' and RoleId in(6,7) and Status='active'";
     			
     		}
             
             
		      remainingRecords= (String) jdbcTemplate.queryForObject(sqlnew, String.class);
		     
		       list.setNextoffset(lastRowno);
		       list.setRemainingRecords(remainingRecords);
		      list.setPhysicians(physicians);
		      list.setStatus("Success");
		}
		else
		{
			list.setStatus("Not Found");
		}
		
		return list;
	}
	
	
	
	public Physician register(Physician physician)
	{
		uuid= Generators.timeBasedGenerator().generate();
		Physician registerPhysician=new Physician();
		Message msg=new Message();
		Address addr=new Address();
		String tenantId = null;
		String rootId=null;
		String companyId=null;
		if(physician.getTenantId()!=null)
		{
			tenantId=physician.getTenantId().toString();
			rootId=getTenantRootId(tenantId);
		}
		else if(physician.getCompanyId()!=null)		
		{
			tenantId=getTenantId(physician.getCompanyId().toString());
			rootId=getTenantRootId(tenantId);
			companyId=physician.getCompanyId().toString();
			
		}
		String sql="select * from Profile where Email=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,physician.getEmail());
		if(rows.size()>0)
		{
		
			msg.setErrorMessage("Already registered");
			registerPhysician.setMessage(msg);
		return registerPhysician;	
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
		
			
			String profilesql="Insert into Profile(RootId,PhysicianId,CompanyId,TenantId,RoleId,FirstName,LastName,Email,Phone,Gender,Dob,Created_On,Street,StreetAdditional,PostalCode,City,State,Country,Status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertprofile=jdbcTemplate.update(profilesql,rootId,uuid.toString(),companyId,tenantId,physician.getRoleId(),physician.getFirstName(),physician.getLastName(),physician.getEmail(),physician.getPhone(),physician.getGender(),physician.getDob(),timestamp.getTime(),physician.getAddress().getStreet(),physician.getAddress().getStreetAdditional(),physician.getAddress().getPostalcode(),physician.getAddress().getCity(),physician.getAddress().getState(),physician.getAddress().getCountry(),"active");
			
						
			String usersql="Insert into Physician(PhysicianId,CompanyId,TenantId,RootId,Status,Created_On) values (?,?,?,?,?,?)";
			int insertphysician=jdbcTemplate.update(usersql,uuid.toString(),companyId,tenantId,rootId,"active",timestamp.getTime());
			
			
			String loginsql="Insert into Login(UserName,Password,UUID,RoleId) values (?,?,?,?)";
			int insertlogin=jdbcTemplate.update(loginsql,physician.getEmail(),randompassword,uuid.toString(),physician.getRoleId());
			
			String addresssql="Insert into Address(UUID,RoleId,Street,StreetAdditional,PostalCode,City,State,Country) values(?,?,?,?,?,?,?,?)";
			int insertaddress=jdbcTemplate.update(addresssql,uuid.toString(),physician.getRoleId(),physician.getAddress().getStreet(),physician.getAddress().getStreetAdditional(),physician.getAddress().getPostalcode(),physician.getAddress().getCity(),physician.getAddress().getState(),physician.getAddress().getCountry());
			
			if(insertprofile>0&&insertphysician>0&&insertlogin>0&&insertaddress>0)
			{
				emailService.sendMail_Register(physician.getEmail(),randompassword);
				msg.setSuccessMessage("Registered Successfully.Please check your email before proceeding.");
				registerPhysician.setMessage(msg);
				registerPhysician.setPhysicianuuid(uuid);
			
			}
			
			
			
			
			
		return registerPhysician;
		}
	}

	
	public Message updateById(Physician physician)
	{
		
		Message msg=new Message();
		Address addr=new Address();
				
		String companyId=getPhysicianCompanyId(physician.getPhysicianuuid().toString());
		System.out.println("cmpyId"+companyId);
		String tenantId=getPhysicianTenantId(physician.getPhysicianuuid().toString());
		System.out.println("cmpyId"+tenantId);
		String rootId=getTenantRootId(tenantId);
		System.out.println("cmpyId"+rootId);
		String profilesql=null;
		if(companyId==null)
		{
			profilesql="Update Profile set FirstName=?,LastName=?,Phone=?,Gender=?,Dob=?,Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=?,Modified_On=? where TenantId=? and RootId=? and CompanyId is null and PhysicianId=? and patientId is null ";
	     
		}
		else
		{
			profilesql="Update Profile set FirstName=?,LastName=?,Phone=?,Gender=?,Dob=?,Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=?,Modified_On=? where TenantId=? and RootId=? and CompanyId='"+companyId+"' and PhysicianId=? and patientId is null ";
		     
		}
		int updateProfile=jdbcTemplate.update(profilesql,physician.getFirstName(),physician.getLastName(),physician.getPhone(),physician.getGender(),physician.getDob(),physician.getAddress().getStreet(),physician.getAddress().getStreetAdditional(),physician.getAddress().getPostalcode(),physician.getAddress().getCity(),physician.getAddress().getState(),physician.getAddress().getCountry(),timestamp.getTime(),tenantId,rootId,physician.getPhysicianuuid());
			
		
		String usersql="Update Physician set Modified_On=? where PhysicianId=?";
		int updatephysician=jdbcTemplate.update(usersql,timestamp.getTime(),physician.getPhysicianuuid());
		
		 String addresssql="Update Address set Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=? where UUID=?";
		 int updateaddress=jdbcTemplate.update(addresssql,physician.getAddress().getStreet(),physician.getAddress().getStreetAdditional(),physician.getAddress().getPostalcode(),physician.getAddress().getCity(),physician.getAddress().getState(),physician.getAddress().getCountry(),physician.getPhysicianuuid());
		 
		
		if(updateProfile>0&&updatephysician>0&&updateaddress>0)
		{
			msg.setSuccessMessage("Update Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Update Details");
		}
		return msg;
	}
	
	public Message deleteById(Physician physician)
	{
		Message msg=new Message();
		
		int deleteProfile=0;
		
		String companyId=getPhysicianCompanyId(physician.getPhysicianuuid().toString());
		String tenantId=getPhysicianTenantId(physician.getPhysicianuuid().toString());
		String rootId=getTenantRootId(tenantId);
		
		String loginsql="Delete FROM Login WHERE UUID=?";

		int deleteLogin=jdbcTemplate.update(loginsql,physician.getPhysicianuuid());
		
		String usersql="Update Physician set Status='inactive' where PhysicianId=?";
		int deletephysician=jdbcTemplate.update(usersql,physician.getPhysicianuuid());
		if(companyId==null)
		{
		deleteProfile=jdbcTemplate.update("Update Profile set Status='inactive' where TenantId=? and RootId=? and CompanyId is null  and PhysicianId=? and patientId is null",tenantId,rootId,physician.getPhysicianuuid());
		}
		else
		{
			deleteProfile=jdbcTemplate.update("Update Profile set Status='inactive' where TenantId=? and RootId=? and CompanyId=? and PhysicianId=? and patientId is null",tenantId,rootId,companyId,physician.getPhysicianuuid());

		}
		if(deleteLogin>0&&deletephysician>0&&deleteProfile>0)
		{
			msg.setSuccessMessage("Delete Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Delete Details");
		}
		return msg;
	}
	
	public String getTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Company WHERE CompanyId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getTenantExist(String id)
	{
		
		String sql = "SELECT * FROM Physician WHERE TenantId=? and Status='active' ";
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
	public String getCompanyExist(String id)
	{
		
		String sql = "SELECT * FROM Physician WHERE CompanyId=? and Status='active' ";
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
	
	public Address getPhysicianAddress(String Id)
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
