package com.iThingsFoundation.IThingsFramework.Company;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.uuid.Generators;
import com.iThingsFoundation.IThingsFramework.Tenant.Tenant;
import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.EmailService;
import com.iThingsFoundation.IThingsFramework.common.Filters;
import com.iThingsFoundation.IThingsFramework.common.Message;



@Service
public class CompanyService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private EmailService emailService;
	
	UUID uuid;
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());


	public CompanyList getCompanyDetails(String batchsize,String offset,Company cust) throws JsonMappingException, JsonProcessingException,NullPointerException 
	{
		ArrayList<Company> companies=new ArrayList<>();
		CompanyList list=new CompanyList();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Filters fil=new Filters() ;
		String remainingRecords=null;
		String lastRowno=null;
		int id=0;
		
		String exist=getTenantExist(cust.getTenantId().toString());
		if(exist=="yes")
		{
		String sql="select t.* from (select @rownum:=@rownum+1 rownumber, t.* from Profile t cross join (SELECT @rownum:=0) r where ";
		
		String lastpart="Status='active' and TenantId='"+cust.getTenantId()+"' and RoleId in(4,5) order by ProfileId asc ) t limit "+offset+","+batchsize+"";
		String sql2="SELECT count(ProfileId) FROM Profile where ";
		 if(cust.getFilter()!=null)
			 
		 {
			 //String sql3;
			      if(cust.getFilter().getFirstName()!=null)
			      {
			    	  sql+= "FirstName like '"+cust.getFilter().getFirstName()+"%' and "; 
			    	  sql2+= "FirstName like '"+cust.getFilter().getFirstName()+"%' and ";
			      }
			      if(cust.getFilter().getEmail()!=null)
			      {
			    	  sql+= "Email like '"+cust.getFilter().getEmail()+"%' and "; 
			    	  sql2+= "Email like '"+cust.getFilter().getEmail()+"%' and "; 
			    	  
			      }
			      if(cust.getFilter().getPhone()!=null)
			      {
			    	  sql+= "Phone like '"+cust.getFilter().getPhone()+"%' and "; 
			    	  sql2+= "Phone like '"+cust.getFilter().getPhone()+"%' and "; 
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
			    
			  Company company=new Company();
			  String cmpId=(String)row.get("CompanyId");
			  company.setCustomeruuid(row.get("CompanyId"));
			  company.setFirstName((String)row.get("FirstName"));
			  company.setLastName((String)row.get("LastName"));
			  company.setPhone((String)row.get("Phone"));
			  company.setEmail((String)row.get("Email"));
			  company.setGender((String)row.get("Gender"));
			  String dob=dateFormat.format(row.get("Dob")).toString();
			  company.setDob(dob);
			    
			  Address address=new Address();
			      address=getCompanyAddress(cmpId);
			
			  company.setAddress(address);
			  company.setRoleId(row.get("RoleId").toString());
			  double val=(double)row.get("rownumber");
			  int rowno=(int) Math.round(val);
			   lastRowno=Integer.toString(rowno);
			   
			   id=(int) row.get("ProfileId");
			      
			  companies.add(company);
	        }
		 
		     String sqlnew = sql2+" ProfileId>"+id+" and TenantId='"+cust.getTenantId()+"'and RoleId in(4,5) and Status='active' ";
            
		     remainingRecords= (String) jdbcTemplate.queryForObject(sqlnew, String.class);
		     
		       list.setNextoffset(lastRowno);
		       list.setRemainingRecords(remainingRecords);
		      list.setCompanies(companies);
		      list.setStatus("Success");
		}
		else
		{
			list.setStatus("Not Found");
		}
		
		return list;
	}

	
	public Company register(Company company)
	{
		uuid= Generators.timeBasedGenerator().generate();
		Company registerCompany=new Company();
		Message msg=new Message();
		Address addr=new Address();
		String rootId=getTenantRootId(company.getTenantId().toString());
		String sql="select * from Profile where Email=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,company.getEmail());
		if(rows.size()>0)
		{
		
			msg.setErrorMessage("Already registered");
			registerCompany.setMessage(msg);
		return registerCompany;	
		}
		else
		{
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            String randompassword= RandomStringUtils.random(10,characters);
			
			
			//change loginpasswrd to generated String line no 67
			//BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			
			//String generatedString  = passwordEncoder.encode(randompassword);
			//System.out.println(randompassword+"----aaaaa---"+generatedString);
		    
		   
			//String profilesql="Insert into Profile(CustomerUUId,TenantId,RoleId,Name,Email,Phone,Created_On,Street,Street2,PostalCode,City,Country) values (?,?,?,?,?,?,?,?,?,?,?,?)";
			//int insertprofile=jdbcTemplate.update(profilesql,uuid.toString(),customer.getTenantId(),customer.getRoleId(),customer.getFirstName(),customer.getEmail(),customer.getPhone(),timestamp.getTime(),customer.getAddress().getStreet(),customer.getAddress().getStreetAdditional(),customer.getAddress().getPostalcode(),customer.getAddress().getCity(),customer.getAddress().getCountry());
			
            String profilesql="Insert into Profile(RootId,CompanyId,TenantId,RoleId,FirstName,LastName,Email,Phone,Gender,Dob,Created_On,Street,StreetAdditional,PostalCode,City,State,Country,Status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertprofile=jdbcTemplate.update(profilesql,rootId,uuid.toString(),company.getTenantId(),company.getRoleId(),company.getFirstName(),company.getLastName(),company.getEmail(),company.getPhone(),company.getGender(),company.getDob(),timestamp.getTime(),company.getAddress().getStreet(),company.getAddress().getStreetAdditional(),company.getAddress().getPostalcode(),company.getAddress().getCity(),company.getAddress().getState(),company.getAddress().getCountry(),"active");
			
			
			
			String companysql="Insert into Company(CompanyId,TenantId,RootId,Status,Created_On) values (?,?,?,?,?)";
			int insertcompany=jdbcTemplate.update(companysql,uuid.toString(),company.getTenantId(),rootId,"active",timestamp.getTime());
			
			String loginsql="Insert into Login(UserName,Password,UUID,RoleId) values (?,?,?,?)";
			int insertlogin=jdbcTemplate.update(loginsql,company.getEmail(),randompassword,uuid.toString(),company.getRoleId());
			
			String addresssql="Insert into Address(UUID,RoleId,Street,StreetAdditional,PostalCode,City,State,Country) values(?,?,?,?,?,?,?,?)";
			int insertaddress=jdbcTemplate.update(addresssql,uuid.toString(),company.getRoleId(),company.getAddress().getStreet(),company.getAddress().getStreetAdditional(),company.getAddress().getPostalcode(),company.getAddress().getCity(),company.getAddress().getState(),company.getAddress().getCountry());
			
			if(insertprofile>0&&insertcompany>0&&insertlogin>0&&insertaddress>0)
			{
				emailService.sendMail_Register(company.getEmail(),randompassword);
				msg.setSuccessMessage("Registered Successfully.Please check your email before proceeding.");
				registerCompany.setMessage(msg);
				registerCompany.setCustomeruuid(uuid);
			
			}
			
			
			
			
			
		return registerCompany;
		}
	}
	
	
	
	public Message updateById(Company company)
	{
		
		Message msg=new Message();
		Address addr=new Address();
		 String tenantId=getCompanyTenantId(company.getCompanyuuid().toString());
		 
		 String rootId=getTenantRootId(tenantId);
		 String profilesql="Update Profile set FirstName=?,LastName=?,Phone=?,Gender=?,Dob=?,Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=?,Modified_On=? where TenantId=? and RootId=? and CompanyId=? and PhysicianId is null and patientId is null ";
	     int updateProfile=jdbcTemplate.update(profilesql,company.getFirstName(),company.getLastName(),company.getPhone(),company.getGender(),company.getDob(),company.getAddress().getStreet(),company.getAddress().getStreetAdditional(),company.getAddress().getPostalcode(),company.getAddress().getCity(),company.getAddress().getState(),company.getAddress().getCountry(),timestamp.getTime(),tenantId,rootId,company.getCompanyuuid());
			
		 
		 String companysql="Update Company set Modified_On=? where CompanyId=?";
		 int updateCompany=jdbcTemplate.update(companysql,timestamp.getTime(),company.getCompanyuuid());
		
		 String addresssql="Update Address set Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=? where UUID=?";
		 int updateaddress=jdbcTemplate.update(addresssql,company.getAddress().getStreet(),company.getAddress().getStreetAdditional(),company.getAddress().getPostalcode(),company.getAddress().getCity(),company.getAddress().getState(),company.getAddress().getCountry(),company.getCompanyuuid());
		 
		 if(updateProfile>0&&updateCompany>0&&updateaddress>0)
		{
			msg.setSuccessMessage("Update Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Update Details");
		}
		return msg;
	}
	
	public Message deleteById(Company company)
	{
		Message msg=new Message();
		String tenantId=getCompanyTenantId(company.getCompanyuuid().toString());
		 String rootId=getTenantRootId(tenantId);
		 
		String loginsql="Delete FROM Login WHERE UUID=?";
		int deleteLogin=jdbcTemplate.update(loginsql,company.getCompanyuuid());
		
		String companysql="Update Company set Status='inactive' where CompanyId=?";
		int deleteCompany=jdbcTemplate.update(companysql,company.getCompanyuuid());
		
		int deleteProfile=jdbcTemplate.update("Update Profile set Status='inactive' where TenantId=? and RootId=? and CompanyId=? and PhysicianId is null and patientId is null",tenantId,rootId,company.getCompanyuuid());
	    
		if(deleteLogin>0&&deleteCompany>0&&deleteProfile>0)
		{
			msg.setSuccessMessage("Delete Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Delete Details");
		}
		return msg;
	}
	
	public String getTenantExist(String id)
	{
		
		String sql = "SELECT * FROM Company WHERE TenantId=? and Status='active' ";
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
	
	public String getCompanyTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Company WHERE CompanyId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public Address getCompanyAddress(String Id)
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
