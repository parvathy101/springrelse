package com.iThingsFoundation.IThingsFramework.Tenant;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.uuid.Generators;
import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.EmailService;
import com.iThingsFoundation.IThingsFramework.common.Filters;
import com.iThingsFoundation.IThingsFramework.common.Message;

@Service
public class TenantService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private EmailService emailService;
	
	UUID uuid ;
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	
	
	
	public TenantList getTenantDetails(String batchsize,String offset,Tenant tenant) throws JsonMappingException, JsonProcessingException,NullPointerException 
	{
		ArrayList<Tenant> tenants=new ArrayList<>();
		TenantList list=new TenantList();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Filters fil=new Filters() ;
		String remainingRecords=null;
		String lastRowno=null;
		int id=0;
		
		String exist=getRootExist(tenant.getRootId().toString());
		if(exist=="yes")
		{
		String sql="select t.* from (select @rownum:=@rownum+1 rownumber, t.* from Profile t cross join (SELECT @rownum:=0) r where ";
		
		String lastpart="Status='active' and RootId='"+tenant.getRootId()+"' and RoleId in(2,3) order by ProfileId asc ) t limit "+offset+","+batchsize+"";
		String sql2="SELECT count(ProfileId) FROM Profile where ";
		 if(tenant.getFilter()!=null)
			 
		 {
			 //String sql3;
			      if(tenant.getFilter().getFirstName()!=null)
			      {
			    	  sql+= "FirstName like '"+tenant.getFilter().getFirstName()+"%' and "; 
			    	  sql2+= "FirstName like '"+tenant.getFilter().getFirstName()+"%' and ";
			      }
			      if(tenant.getFilter().getEmail()!=null)
			      {
			    	  sql+= "Email like '"+tenant.getFilter().getEmail()+"%' and "; 
			    	  sql2+= "Email like '"+tenant.getFilter().getEmail()+"%' and "; 
			    	  
			      }
			      if(tenant.getFilter().getPhone()!=null)
			      {
			    	  sql+= "Phone like '"+tenant.getFilter().getPhone()+"%' and "; 
			    	  sql2+= "Phone like '"+tenant.getFilter().getPhone()+"%' and "; 
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
			    
			  Tenant ten=new Tenant();
			  ten.setUuid(row.get("TenantId"));
			  String tenntId=(String)row.get("TenantId");
			  ten.setFirstName((String)row.get("FirstName"));
			  ten.setLastName((String)row.get("LastName"));
			  ten.setPhone((String)row.get("Phone"));
			  ten.setEmail((String)row.get("Email"));
			  ten.setGender((String)row.get("Gender"));
			  String dob=dateFormat.format(row.get("Dob")).toString();
			  ten.setDob(dob);
			 
			  Address address=new Address();
			  address=getTenantAddress(tenntId);
				  
		
				  ten.setAddress(address);
				  ten.setRoleId(row.get("RoleId").toString());
			  double val=(double)row.get("rownumber");
			  int rowno=(int) Math.round(val);
			   lastRowno=Integer.toString(rowno);
			   
			   id=(int) row.get("ProfileId");
			      
			   tenants.add(ten);
	        }
		 
		     String sqlnew = sql2+" ProfileId>"+id+" and RootId='"+tenant.getRootId()+"'and RoleId in(2,3) and Status='active' ";
            
		     remainingRecords= (String) jdbcTemplate.queryForObject(sqlnew, String.class);
		     
		       list.setNextoffset(lastRowno);
		       list.setRemainingRecords(remainingRecords);
		      list.setTenants(tenants);
		      list.setStatus("Success");
		}
		else
		{
			list.setStatus("Not Found");
		}
		
		return list;
	}

	
	
	
	
	public Tenant register(Tenant tenant)
	{
		uuid= Generators.timeBasedGenerator().generate();
		Tenant registerTenant=new Tenant();
		Message msg=new Message();
		Address addr=new Address();
	
		String sql="select * from Profile where Email=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,tenant.getEmail());
		if(rows.size()>0)
		{
		
			msg.setErrorMessage("Already registered");
			registerTenant.setMessage(msg);
		return registerTenant;	
		}
		else
		{
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			String randompassword= RandomStringUtils.random(10,characters);
			
			
			//change loginpasswrd to generated String line no 67
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			
			String generatedString  = passwordEncoder.encode(randompassword);
			System.out.println(randompassword+"----aaaaa---"+generatedString);
		   
			String profilesql="Insert into Profile(RootId,TenantId,RoleId,FirstName,LastName,Email,Phone,Gender,Dob,Created_On,Street,StreetAdditional,PostalCode,City,State,Country,Status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertprofile=jdbcTemplate.update(profilesql,tenant.getRootId(),uuid.toString(),tenant.getRoleId(),tenant.getFirstName(),tenant.getLastName(),tenant.getEmail(),tenant.getPhone(),tenant.getGender(),tenant.getDob(),timestamp.getTime(),tenant.getAddress().getStreet(),tenant.getAddress().getStreetAdditional(),tenant.getAddress().getPostalcode(),tenant.getAddress().getCity(),tenant.getAddress().getState(),tenant.getAddress().getCountry(),"active");
			
			
			String tenantsql="Insert into Tenant(TenantId,RootId,Status,Created_On) values (?,?,?,?)";
			int inserttenant=jdbcTemplate.update(tenantsql,uuid.toString(),tenant.getRootId(),"active",timestamp.getTime());
			
			
			String loginsql="Insert into Login(UserName,Password,UUID,RoleId) values (?,?,?,?)";
			int insertlogin=jdbcTemplate.update(loginsql,tenant.getEmail(),generatedString,uuid.toString(),tenant.getRoleId());
			
			String addresssql="Insert into Address(UUID,RoleId,Street,StreetAdditional,PostalCode,City,State,Country) values(?,?,?,?,?,?,?,?)";
			int insertaddress=jdbcTemplate.update(addresssql,uuid.toString(),tenant.getRoleId(),tenant.getAddress().getStreet(),tenant.getAddress().getStreetAdditional(),tenant.getAddress().getPostalcode(),tenant.getAddress().getCity(),tenant.getAddress().getState(),tenant.getAddress().getCountry());
			
			
			if(insertprofile>0&&inserttenant>0&&insertlogin>0&&insertaddress>0)
			{
				emailService.sendMail_Register(tenant.getEmail(),randompassword);
				msg.setSuccessMessage("Registered Successfully.Please check your email before proceeding.");
				registerTenant.setMessage(msg);
				registerTenant.setUuid(uuid);
				registerTenant.setStatus("Success");
			}
			
			
			
			
			
		return registerTenant;
		}
	}
	public Message updateById(Tenant tenant)
	{
		
		Message msg=new Message();
		Address addr=new Address();
		String rootId=getTenantRootId(tenant.getUuid().toString());
		String profilesql="Update Profile set FirstName=?,LastName=?,Phone=?,Gender=?,Dob=?,Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=?,Modified_On=? where TenantId=? and RootId=? and CompanyId is null and PhysicianId is null and patientId is null ";
		int updateProfile=jdbcTemplate.update(profilesql,tenant.getFirstName(),tenant.getLastName(),tenant.getPhone(),tenant.getGender(),tenant.getDob(),tenant.getAddress().getStreet(),tenant.getAddress().getStreetAdditional(),tenant.getAddress().getPostalcode(),tenant.getAddress().getCity(),tenant.getAddress().getState(),tenant.getAddress().getCountry(),timestamp.getTime(),tenant.getUuid(),rootId);
		
		String tenantsql="Update Tenant set Modified_On=? where TenantId=?";
		int updateTenant=jdbcTemplate.update(tenantsql,timestamp.getTime(),tenant.getUuid());
		
		String addresssql="Update Address set Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=? where UUID=?";
		 int updateaddress=jdbcTemplate.update(addresssql,tenant.getAddress().getStreet(),tenant.getAddress().getStreetAdditional(),tenant.getAddress().getPostalcode(),tenant.getAddress().getCity(),tenant.getAddress().getState(),tenant.getAddress().getCountry(),tenant.getUuid());
		 
		//int updateProfile=jdbcTemplate.update("Update Profile set Name='"+tenant.getFirstName()+"',Phone='"+tenant.getPhone()+"',Street='"+tenant.getAddress().getStreet()+"',Street2='"+tenant.getAddress().getStreetAdditional()+"',PostalCode='"+tenant.getAddress().getPostalcode()+"',City='"+tenant.getAddress().getCity()+"',Country='"+tenant.getAddress().getCountry()+"' where TenantId='"+tenant.getUuid()+"'");
		//int updateTenant=jdbcTemplate.update("Update Tenant set  Name='"+tenant.getFirstName()+"',Phone='"+tenant.getPhone()+"',Street='"+tenant.getAddress().getStreet()+"',Street2='"+tenant.getAddress().getStreetAdditional()+"',PostalCode='"+tenant.getAddress().getPostalcode()+"',City='"+tenant.getAddress().getCity()+"',Country='"+tenant.getAddress().getCountry()+"'where Id='"+tenant.getUuid()+"'");
		if(updateProfile>0&&updateTenant>0&&updateaddress>0)
		{
			msg.setSuccessMessage("Update Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Update Details");
		}
		return msg;
	}
	
	public Message deleteById(Object id)
	{
		Message msg=new Message();
		
		String rootId=getTenantRootId(id.toString());
		int deleteLogin=jdbcTemplate.update("Delete FROM Login WHERE UUID=? ", id);
	
	
		int deleteTenant=jdbcTemplate.update("Update Tenant set Status='inactive' where TenantId=?",id);
	    
		
			
		int deleteProfile=jdbcTemplate.update("Update Profile set Status='inactive' where TenantId=? and RootId=? and CompanyId is null and PhysicianId is null and patientId is null",id,rootId);
	    
		
		if(deleteLogin>0&&deleteTenant>0&&deleteProfile>0)
		{
			msg.setSuccessMessage("Delete Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Delete Details");
		}
		return msg;
	}

	
	public String getTenantRootId(String id)
	{
		
		String sql = "SELECT RootId FROM Tenant WHERE TenantId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getRootExist(String id)
	{
		
		String sql = "SELECT * FROM Root WHERE RootId=? and Status='active' ";
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
	
	public Address getTenantAddress(String Id)
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
