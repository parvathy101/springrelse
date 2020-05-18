package com.iThingsFoundation.IThingsFramework.Tenant;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.uuid.Generators;
import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.EmailService;
import com.iThingsFoundation.IThingsFramework.common.Message;

@Service
public class TenantService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private EmailService emailService;
	
	UUID uuid ;
	
	
	public Tenant register(Tenant tenant)
	{
		uuid= Generators.timeBasedGenerator().generate();
		Tenant registerTenant=new Tenant();
		Message msg=new Message();
		Address addr=new Address();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
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
			
			/*
			//change loginpasswrd to generated String line no 67
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			
			String generatedString  = passwordEncoder.encode(randompassword);
			System.out.println(randompassword+"----aaaaa---"+generatedString);
		    */
			String profilesql="Insert into Profile(TenantId,RoleId,Name,Email,Phone,Created_On,Street,Street2,PostalCode,City,Country) values (?,?,?,?,?,?,?,?,?,?,?)";
			int insertprofile=jdbcTemplate.update(profilesql,uuid.toString(),1,tenant.getFirstName(),tenant.getEmail(),tenant.getPhone(),timestamp.getTime(),tenant.getAddress().getStreet(),tenant.getAddress().getStreetAdditional(),tenant.getAddress().getPostalcode(),tenant.getAddress().getCity(),tenant.getAddress().getCountry());
			
			
			String tenantsql="Insert into Tenant(Id,Name,Phone,Email,Status,Street,Street2,PostalCode,City,Country) values (?,?,?,?,?,?,?,?,?,?)";
			int inserttenant=jdbcTemplate.update(tenantsql,uuid.toString(),tenant.getFirstName(),tenant.getPhone(),tenant.getEmail(),"active",tenant.getAddress().getStreet(),tenant.getAddress().getStreetAdditional(),tenant.getAddress().getPostalcode(),tenant.getAddress().getCity(),tenant.getAddress().getCountry());
			
			
			String loginsql="Insert into Login1(UserName,Password,TenantId,RoleId) values (?,?,?,?)";
			int insertlogin=jdbcTemplate.update(loginsql,tenant.getEmail(),randompassword,uuid.toString(),1);
			
			if(insertprofile>0&&inserttenant>0&&insertlogin>0)
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
		
		String profilesql="Update Profile set Name=?,Phone=?,Street=?,Street2=?,PostalCode=?,City=?,Country=? where TenantId=?";
		int updateProfile=jdbcTemplate.update(profilesql,tenant.getFirstName(),tenant.getPhone(),tenant.getAddress().getStreet(),tenant.getAddress().getStreetAdditional(),tenant.getAddress().getPostalcode(),tenant.getAddress().getCity(),tenant.getAddress().getCountry(),tenant.getUuid());
		
		String tenantsql="Update Tenant set Name=?,Phone=?,Street=?,Street2=?,PostalCode=?,City=?,Country=? where Id=?";
		int updateTenant=jdbcTemplate.update(tenantsql,tenant.getFirstName(),tenant.getPhone(),tenant.getAddress().getStreet(),tenant.getAddress().getStreetAdditional(),tenant.getAddress().getPostalcode(),tenant.getAddress().getCity(),tenant.getAddress().getCountry(),tenant.getUuid());
		
		
		//int updateProfile=jdbcTemplate.update("Update Profile set Name='"+tenant.getFirstName()+"',Phone='"+tenant.getPhone()+"',Street='"+tenant.getAddress().getStreet()+"',Street2='"+tenant.getAddress().getStreetAdditional()+"',PostalCode='"+tenant.getAddress().getPostalcode()+"',City='"+tenant.getAddress().getCity()+"',Country='"+tenant.getAddress().getCountry()+"' where TenantId='"+tenant.getUuid()+"'");
		//int updateTenant=jdbcTemplate.update("Update Tenant set  Name='"+tenant.getFirstName()+"',Phone='"+tenant.getPhone()+"',Street='"+tenant.getAddress().getStreet()+"',Street2='"+tenant.getAddress().getStreetAdditional()+"',PostalCode='"+tenant.getAddress().getPostalcode()+"',City='"+tenant.getAddress().getCity()+"',Country='"+tenant.getAddress().getCountry()+"'where Id='"+tenant.getUuid()+"'");
		if(updateProfile>0&&updateTenant>0)
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
		
		
		int deleteLogin=jdbcTemplate.update("Delete FROM Login1 WHERE TenantId=? ", id);
	
	
		int deleteTenant=jdbcTemplate.update("Update Tenant set Status='inactive' where Id=? ",id);
	
		if(deleteLogin>0&&deleteTenant>0)
		{
			msg.setSuccessMessage("Delete Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Delete Details");
		}
		return msg;
	}

}
