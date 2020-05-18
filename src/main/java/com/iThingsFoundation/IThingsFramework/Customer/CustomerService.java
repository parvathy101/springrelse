package com.iThingsFoundation.IThingsFramework.Customer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class CustomerService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private EmailService emailService;
	
	UUID uuid;
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());


	public CustomerList getCustomerDetails(String batchsize,String offset,Customer cust) throws JsonMappingException, JsonProcessingException,NullPointerException 
	{
		ArrayList<Customer> customers=new ArrayList<>();
		CustomerList list=new CustomerList();
		
		
		Filters fil=new Filters() ;
		String remainingRecords=null;
		String lastRowno=null;
		int id=0;
		
		String exist=getTenantExist(cust.getTenantId().toString());
		if(exist=="yes")
		{
		String sql="select t.* from (select @rownum:=@rownum+1 rownumber, t.* from Customer t cross join (SELECT @rownum:=0) r where ";
		
		String lastpart="Status='active' and TenantId='"+cust.getTenantId()+"' order by Id asc ) t limit "+offset+","+batchsize+"";
		String sql2="SELECT count(Id) FROM Customer where ";
		 if(cust.getFilter()!=null)
			 
		 {
			 //String sql3;
			      if(cust.getFilter().getFirstName()!=null)
			      {
			    	  sql+= "Name like '"+cust.getFilter().getFirstName()+"%' and "; 
			    	  sql2+= "Name like '"+cust.getFilter().getFirstName()+"%' and ";
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
			    
			  Customer customer=new Customer();
			  customer.setCustomeruuid(row.get("CustomerUUID"));
			  customer.setFirstName((String)row.get("Name"));
			  customer.setPhone((String)row.get("Phone"));
			  customer.setEmail((String)row.get("Email"));
			 
			  Address address=new Address();
				  address.setStreet((String)row.get("Street"));
				  address.setStreetAdditional((String)row.get("Street2"));
				  address.setPostalcode((String)row.get("PostalCode"));
				  address.setCity((String)row.get("City"));
				  address.setCountry((String)row.get("Country"));
				  
		
			  customer.setAddress(address);
			  customer.setRoleId((String)row.get("RoleId"));
			  double val=(double)row.get("rownumber");
			  int rowno=(int) Math.round(val);
			   lastRowno=Integer.toString(rowno);
			   
			   id=(int) row.get("Id");
			      
			  customers.add(customer);
	        }
		 
		     String sqlnew = sql2+" Id>"+id+" and TenantId='"+cust.getTenantId()+"' ";
            
		     remainingRecords= (String) jdbcTemplate.queryForObject(sqlnew, String.class);
		     
		       list.setNextoffset(lastRowno);
		       list.setRemainingRecords(remainingRecords);
		      list.setCustomers(customers);
		      list.setStatus("Success");
		}
		else
		{
			list.setStatus("Not Found");
		}
		
		return list;
	}

	
	public Customer register(Customer customer)
	{
		uuid= Generators.timeBasedGenerator().generate();
		Customer registerCustomer=new Customer();
		Message msg=new Message();
		Address addr=new Address();
		
		String sql="select * from Profile where Email=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,customer.getEmail());
		if(rows.size()>0)
		{
		
			msg.setErrorMessage("Already registered");
			registerCustomer.setMessage(msg);
		return registerCustomer;	
		}
		else
		{
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            String randompassword= RandomStringUtils.random(10,characters);
			
			
			//change loginpasswrd to generated String line no 67
			//BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			
			//String generatedString  = passwordEncoder.encode(randompassword);
			//System.out.println(randompassword+"----aaaaa---"+generatedString);
		    
		   
			String profilesql="Insert into Profile(CustomerUUId,TenantId,RoleId,Name,Email,Phone,Created_On,Street,Street2,PostalCode,City,Country) values (?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertprofile=jdbcTemplate.update(profilesql,uuid.toString(),customer.getTenantId(),customer.getRoleId(),customer.getFirstName(),customer.getEmail(),customer.getPhone(),timestamp.getTime(),customer.getAddress().getStreet(),customer.getAddress().getStreetAdditional(),customer.getAddress().getPostalcode(),customer.getAddress().getCity(),customer.getAddress().getCountry());
			
			String customersql="Insert into Customer(CustomerUUId,TenantId,Name,Phone,Email,RoleId,Status,Created_On,Street,Street2,PostalCode,City,Country) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertcustomer=jdbcTemplate.update(customersql,uuid.toString(),customer.getTenantId(),customer.getFirstName(),customer.getPhone(),customer.getEmail(),customer.getRoleId(),"active",timestamp.getTime(),customer.getAddress().getStreet(),customer.getAddress().getStreetAdditional(),customer.getAddress().getPostalcode(),customer.getAddress().getCity(),customer.getAddress().getCountry());
			
			String loginsql="Insert into Login1(UserName,Password,CustomerUUId,TenantId,RoleId) values (?,?,?,?,?)";
			int insertlogin=jdbcTemplate.update(loginsql,customer.getEmail(),randompassword,uuid.toString(),customer.getTenantId(),customer.getRoleId());
			
			if(insertprofile>0&&insertcustomer>0&&insertlogin>0)
			{
				emailService.sendMail_Register(customer.getEmail(),randompassword);
				msg.setSuccessMessage("Registered Successfully.Please check your email before proceeding.");
				registerCustomer.setMessage(msg);
				registerCustomer.setCustomeruuid(uuid);
			
			}
			
			
			
			
			
		return registerCustomer;
		}
	}
	
	
	
	public Message updateById(Customer customer)
	{
		
		Message msg=new Message();
		Address addr=new Address();
		 String profilesql="Update Profile set Name=?,Phone=?,RoleId=?,Modified_On=?,Street=?,Street2=?,PostalCode=?,City=?,Country=? where CustomerUUId=?";
		 int updateProfile=jdbcTemplate.update(profilesql,customer.getFirstName(),customer.getPhone(),customer.getRoleId(),timestamp.getTime(),customer.getAddress().getStreet(),customer.getAddress().getStreetAdditional(),customer.getAddress().getPostalcode(),customer.getAddress().getCity(),customer.getAddress().getCountry(),customer.getCustomeruuid());
		String customersql="Update Customer set Name=?,Phone=?,RoleId=?,Modified_On=?,Street=?,Street2=?,PostalCode=?,City=?,Country=? where CustomerUUId=?";
		 int updateCustomer=jdbcTemplate.update(customersql,customer.getFirstName(),customer.getPhone(),customer.getRoleId(),timestamp.getTime(),customer.getAddress().getStreet(),customer.getAddress().getStreetAdditional(),customer.getAddress().getPostalcode(),customer.getAddress().getCity(),customer.getAddress().getCountry(),customer.getCustomeruuid());
		if(updateProfile>0&&updateCustomer>0)
		{
			msg.setSuccessMessage("Update Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Update Details");
		}
		return msg;
	}
	
	public Message deleteById(Customer customer)
	{
		Message msg=new Message();
		String loginsql="Delete FROM Login1 WHERE CustomerUUId=?";
		int deleteLogin=jdbcTemplate.update(loginsql,customer.getCustomeruuid());
		String customersql="Update Customer set Status='inactive' where CustomerUUId=?";
		int deleteCustomer=jdbcTemplate.update(customersql,customer.getCustomeruuid());
		if(deleteLogin>0&&deleteCustomer>0)
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
		
		String sql = "SELECT * FROM Customer WHERE TenantId=? and Status='active' ";
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
