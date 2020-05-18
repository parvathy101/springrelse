package com.iThingsFoundation.IThingsFramework.Physician;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.uuid.Generators;
import com.iThingsFoundation.IThingsFramework.Customer.Customer;
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
		
		Filters fil=new Filters() ;
		String remainingRecords=null;
		String lastRowno=null;
		int id=0;
		
		String tenantId=null;
		String customerId=null;
		String lastpart=null;
		String exist=null;
		
		
		if(phys.getTenantId()!=null)
		{
			 exist=getTenantExist(phys.getTenantId().toString());
		}
		else if(phys.getCustomerId()!=null)
		{
			exist=getCustomerExist(phys.getCustomerId().toString());
		}
		if(exist=="yes")
		{
			
			
			if(phys.getTenantId()!=null)
			{
				tenantId=phys.getTenantId().toString();
				customerId=null;
		        lastpart="Status='active' and TenantId='"+tenantId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
		        
			}
			else if(phys.getCustomerId()!=null)
			{
				customerId=phys.getCustomerId().toString();
				tenantId=getTenantId(customerId);
			    lastpart="Status='active' and CustomerUUId='"+customerId+"' and TenantId='"+tenantId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
			    
			}
			
		String sql="select t.* from (select @rownum:=@rownum+1 rownumber, t.* from User t cross join (SELECT @rownum:=0) r where ";
		
		//String lastpart="Status='active' and CustomerUUId='"+customerId+"' and TenantId='"+tenantId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
		String sql2="SELECT count(Id) FROM User where ";
		 if(phys.getFilter()!=null)
		 {
			 //String sql3;
			      if(phys.getFilter().getFirstName()!=null)
			      {
			    	  sql+= "Name like '"+phys.getFilter().getFirstName()+"%' and "; 
			    	  sql2+= "Name like '"+phys.getFilter().getFirstName()+"%' and ";
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
		   
	    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		
		//String count=jdbcTemplate.queryForObject("select * from Customer");
		 for (Map<String, Object> row : rows) 
	        {
			  Physician physician=new Physician();
			  physician.setPhysicianuuid(row.get("UserId"));
			  physician.setFirstName((String)row.get("Name"));
			  physician.setPhone((String)row.get("Phone"));
			  physician.setEmail((String)row.get("Email"));
			  Address address=new Address();
			  address.setStreet((String)row.get("Street"));
			  address.setStreetAdditional((String)row.get("Street2"));
			  address.setPostalcode((String)row.get("PostalCode"));
			  address.setCity((String)row.get("City"));
			  address.setCountry((String)row.get("Country"));
			  physician.setAddress(address);
			  physician.setRoleId(String.valueOf((int)row.get("RoleId")));
			  double val=(double)row.get("rownumber");
			  int rowno=(int) Math.round(val);
			   lastRowno=Integer.toString(rowno);
			  
			   
			   id=(int) row.get("Id");
			      
			   physicians.add(physician);
	        }
		 
		     String sqlnew =null;
             if(phys.getTenantId()!=null)
     		{
     			tenantId=phys.getTenantId().toString();
     			customerId=null;
     			 sqlnew = sql2+" Id>"+id+" and TenantId='"+tenantId+"'";
     		}
     		else if(phys.getCustomerId()!=null)
     		{
     			customerId=phys.getCustomerId().toString();
     			tenantId=getTenantId(customerId);
     			sqlnew = sql2+" Id>"+id+" and CustomerUUId='"+customerId+"' and TenantId='"+tenantId+"'";
     			
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
		if(physician.getTenantId()!=null)
		{
			tenantId=physician.getTenantId().toString();
		}
		else if(physician.getCustomerId()!=null)		
		{
			tenantId=getTenantId(physician.getCustomerId().toString());
			
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
		
			String profilesql="Insert into Profile(UserId,CustomerUUId,TenantId,RoleId,Name,Email,Phone,Created_On,Street,Street2,PostalCode,City,Country) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertprofile=jdbcTemplate.update(profilesql,uuid.toString(),physician.getCustomerId(),tenantId,physician.getRoleId(),physician.getFirstName(),physician.getEmail(),physician.getPhone(),timestamp.getTime(),physician.getAddress().getStreet(),physician.getAddress().getStreetAdditional(),physician.getAddress().getPostalcode(),physician.getAddress().getCity(),physician.getAddress().getCountry());
			
			String usersql="Insert into User(UserId,CustomerUUId,TenantId,Name,Phone,Email,RoleId,Status,Created_On,Street,Street2,PostalCode,City,Country) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertphysician=jdbcTemplate.update(usersql,uuid.toString(),physician.getCustomerId(),tenantId,physician.getFirstName(),physician.getPhone(),physician.getEmail(),physician.getRoleId(),"active",timestamp.getTime(),physician.getAddress().getStreet(),physician.getAddress().getStreetAdditional(),physician.getAddress().getPostalcode(),physician.getAddress().getCity(),physician.getAddress().getCountry());
			
			
			String loginsql="Insert into Login1(UserName,Password,UserId,CustomerUUId,TenantId,RoleId) values (?,?,?,?,?,?)";
			int insertlogin=jdbcTemplate.update(loginsql,physician.getEmail(),randompassword,uuid.toString(),physician.getCustomerId(),tenantId,physician.getRoleId());
			
			if(insertprofile>0&&insertphysician>0&&insertlogin>0)
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
		
		String profilesql="Update Profile set Name=?,Phone=?,RoleId=?,Modified_On=?,Street=?,Street2=?,PostalCode=?,City=?,Country=? where UserId=?";
		int updateProfile=jdbcTemplate.update(profilesql,physician.getFirstName(),physician.getPhone(),physician.getRoleId(),+timestamp.getTime(),physician.getAddress().getStreet(),physician.getAddress().getStreetAdditional(),physician.getAddress().getPostalcode(),physician.getAddress().getCity(),physician.getAddress().getCountry(),physician.getPhysicianuuid());
		
		
		String usersql="Update User set Name=?,Phone=?,RoleId=?,Modified_On=?,Street=?,Street2=?,PostalCode=?,City=?,Country=? where UserId=?";
		int updatephysician=jdbcTemplate.update(usersql,physician.getFirstName(),physician.getPhone(),physician.getRoleId(),timestamp.getTime(),physician.getAddress().getStreet(),physician.getAddress().getStreetAdditional(),physician.getAddress().getPostalcode(),physician.getAddress().getCity(),physician.getAddress().getCountry(),physician.getPhysicianuuid());
		if(updateProfile>0&&updatephysician>0)
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
		
		
		String loginsql="Delete FROM Login1 WHERE UserId=?";
		int deleteLogin=jdbcTemplate.update(loginsql,physician.getPhysicianuuid());
		
		String usersql="Update User set Status='inactive' where UserId=?";
		int deletephysician=jdbcTemplate.update(usersql,physician.getPhysicianuuid());
		
		if(deleteLogin>0&&deletephysician>0)
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
		
		String sql = "SELECT TenantId FROM Customer WHERE CustomerUUId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getTenantExist(String id)
	{
		
		String sql = "SELECT * FROM User WHERE TenantId=? and Status='active' ";
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
	public String getCustomerExist(String id)
	{
		
		String sql = "SELECT * FROM User WHERE CustomerUUId=? and Status='active' ";
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
