package com.iThingsFoundation.IThingsFramework.Login;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.iThingsFoundation.IThingsFramework.Device.Device;
import com.iThingsFoundation.IThingsFramework.common.Address1;
import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.Message;



@Service
public class LoginService {
	

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	
	
	public Login loginUser(Login loginDetails,HttpServletRequest request,HttpSession session)
	{
		
		 Login login =new Login();
		 Message msg=new Message();
		/* add this at the time of using encrypted passwrd
		 BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String sqlpasswrd = "SELECT password FROM Login WHERE UserName=?";

	    String passwrd = (String) jdbcTemplate.queryForObject(
	            sqlpasswrd, new Object[] { loginDetails.getUserName() }, String.class);
          System.out.println("psswrd--"+passwrd);
          boolean isPasswordMatch = passwordEncoder.matches(loginDetails.getPassword(),passwrd);
          System.out.println(isPasswordMatch);
          
          if(isPasswordMatch)
          {*/
         String sql = "SELECT  UserName,RoleId,UserId,CustomerUUId,TenantId,PatientId from Login1  where UserName=? and Password=?"; 

		try
		{
		
		
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,loginDetails.getUserName(),loginDetails.getPassword());
			/*add this at the time of using encrypted passwrd 
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,loginDetails.getUserName(),passwrd);
			*/
			System.out.println(rows.size());
			System.out.println(session.getId());
			if(rows.size()>0)
			{
				@SuppressWarnings("unchecked")
				List<String> msgs = (List<String>) request.getSession().getAttribute("UserNames");
				System.out.println(session.getId());
				if (msgs == null) {
					msgs = new ArrayList<>();
					request.getSession().setAttribute("UserNames", msgs);
				}
				msgs.add(loginDetails.getUserName());
				request.getSession().setAttribute("UserNames", msgs);
			 for (Map<String, Object> row : rows) 
		        {
		
				login.setUserName((String)row.get("UserName"));
				login.setRoleId((int)row.get("RoleId"));
				if((int)row.get("RoleId")==1)
				{
					login.setUuid(row.get("TenantId"));
				}
				if((int)row.get("RoleId")==3)
				{
					login.setUuid(row.get("CustomerUUId"));
				}
				if((int)row.get("RoleId")==6||(int)row.get("RoleId")==5)
				{
					login.setUuid(row.get("UserId"));
				}
				if((int)row.get("RoleId")==8)
				{
					login.setUuid(row.get("PatientId"));
				}
				login.setStatus("Success");
				msg.setSuccessMessage("Successfully logged in");
				login.setMessage(msg);
				login.setTokenId(session.getId());
				 
				  
				  
		        }
			
			}
			else
			    {
				login.setStatus("Not Found");
				msg.setErrorMessage("Not Successfully logged in");
				login.setMessage(msg);
							 
				 }
		}
		
		
		catch (Exception e) {
			// TODO: handle exception
			
		}
         /* add this at the time of using encrypted passwrd 
          }
          else
          {
        	  login.setStatus("Not Found");
				msg.setErrorMessage("Not Successfully logged in");
				login.setMessage(msg);
          }*/ 
		return login;
		
		
	}
	
	public LoginDetails getDetails(LoginDetails details)
	{
		LoginDetails getLoginDetails =new LoginDetails();
		
		
		Message msg=new Message();
		
		String mailId=getMailId(details.getUuid(),details.getRoleId());
		if(mailId!=null)
		{
		String sql = "SELECT  * from Profile  where Email=?"; 
		
		try {
			
			List<Map<String, Object>> rows =  jdbcTemplate.queryForList(sql, mailId);
			
			if(rows.size()>0)
			{
			for (Map<String, Object> row : rows) 
	        {
			   if((int)row.get("RoleId")==1)
			   {
				getLoginDetails.setUuid((String)row.get("TenantId"));  
			   }
			   else if((int)row.get("RoleId")==3)
			   {
				   getLoginDetails.setUuid((String)row.get("CustomerUUId")); 
			   }
			   else if((int)row.get("RoleId")==5||(int)row.get("RoleId")==6)
			   {
				   getLoginDetails.setUuid((String)row.get("UserId"));
			   }
			  
				getLoginDetails.setFirstName((String)row.get("Name"));
				getLoginDetails.setPhone((String)row.get("Phone"));
				Address address=new Address();
				  address.setStreet((String)row.get("Street"));
				  address.setStreetAdditional((String)row.get("Street2"));
				  address.setPostalcode((String)row.get("PostalCode"));
				  address.setCity((String)row.get("City"));
				  address.setCountry((String)row.get("Country"));
				getLoginDetails.setAddress(address);
				msg.setSuccessMessage("Successfully Found details");
				getLoginDetails.setMessage(msg);
				getLoginDetails.setRoleId((int)row.get("RoleId"));
				getLoginDetails.setStatus("Success");
				
			 
			  
			  
	        }
			}
			else
			{
				msg.setErrorMessage("Not Found details");
				getLoginDetails.setMessage(msg);
				getLoginDetails.setStatus("Error");
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		}
		else {
			msg.setErrorMessage("Not Found details");
			getLoginDetails.setMessage(msg);
			getLoginDetails.setStatus("Error");
		}
		
		
		
		return getLoginDetails;
		
	}
	
	public Message reset(Login login)
	{
		
		Message msg=new Message();
		/*
		put generatedstring insted of login.getpassword()
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		String generatedString  = passwordEncoder.encode(login.getPassword());
		System.out.println(randompassword+"----aaaaa---"+generatedString);
	    */
		String loginsql="Update Login1 set Password=? where UserName=?";
		int updatepassword=jdbcTemplate.update(loginsql,login.getPassword(),login.getUserName());
		if(updatepassword>0)
		{
			msg.setSuccessMessage("Reset Password Successfully");
		}
		else {
			msg.setErrorMessage("Password Reset Failed");
		}
		return msg;
	}
	
	public String getMailId(String uid,int roleId)
	{
		String sql = null;
		String mailid=null;

		if(roleId==1)
		{
			sql="Select UserName from Login1 where TenantId=? and RoleId=1";
		}
		if(roleId==3)
		{
			
			sql="Select UserName from Login1 where CustomerUUId=? and RoleId=3";
		}
		if(roleId==6||roleId==5)
		{
			
			sql="Select UserName from Login1 where UserId=? and RoleId in(5,6)";
			
		}
		if(roleId==8)
		{
			sql="Select UserName from Login1 where PatientId=? and RoleId=8";
		}
	   // mailid = (String) jdbcTemplate.queryForObject(sql, new Object[] { uid }, String.class);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,uid);
		
		if(rows.size()>0)
		{
		for (Map<String, Object> row : rows) 
        {
			mailid=(String)row.get("UserName");
        }
		}
		else
		{
			mailid=null;
        }
	    return mailid;
	}

}
