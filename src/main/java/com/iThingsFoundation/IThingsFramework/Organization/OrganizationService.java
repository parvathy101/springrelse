package com.iThingsFoundation.IThingsFramework.Organization;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.uuid.Generators;
import com.iThingsFoundation.IThingsFramework.Tenant.Tenant;
import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.EmailService;
import com.iThingsFoundation.IThingsFramework.common.Message;

@Service
public class OrganizationService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private EmailService emailService;
	
	UUID uuid ;
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	

	
	public Organization register(Organization org)
	{
		uuid= Generators.timeBasedGenerator().generate();
		Organization registerOrganization=new Organization();
		Message msg=new Message();
		Address addr=new Address();
	
		String sql="select * from Profile where Email=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,org.getEmail());
		if(rows.size()>0)
		{
		
			msg.setErrorMessage("Already registered");
			registerOrganization.setMessage(msg);
		return registerOrganization;	
		}
		else
		{
			String type=getRootType(org.getRootId().toString());
			System.out.println(type);
			if(type.equals("0"))
			{
				
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			String randompassword= RandomStringUtils.random(10,characters);
			
			
			//change loginpasswrd to generated String line no 67
			//BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			
			//String generatedString  = passwordEncoder.encode(randompassword);
			//System.out.println(randompassword+"----aaaaa---"+generatedString);
		   
			String profilesql="Insert into Profile(RootId,RoleId,FirstName,LastName,Email,Phone,Created_On,Street,StreetAdditional,PostalCode,City,State,Country,Status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insertprofile=jdbcTemplate.update(profilesql,uuid.toString(),1,org.getFirstName(),org.getLastName(),org.getEmail(),org.getPhone(),timestamp.getTime(),org.getAddress().getStreet(),org.getAddress().getStreetAdditional(),org.getAddress().getPostalcode(),org.getAddress().getCity(),org.getAddress().getState(),org.getAddress().getCountry(),"active");
			
			
			String rootsql="Insert into Root(RootId,Name,Phone,Email,RoleId,Type,Status,Created_On) values (?,?,?,?,?,?,?,?)";
			int inserttenant=jdbcTemplate.update(rootsql,uuid.toString(),org.getFirstName(),org.getPhone(),org.getEmail(),1,org.getType(),"active",timestamp.getTime());
			
			
			String loginsql="Insert into Login(UserName,Password,UUID,RoleId) values (?,?,?,?)";
			int insertlogin=jdbcTemplate.update(loginsql,org.getEmail(),randompassword,uuid.toString(),1);
			
			String addresssql="Insert into Address(UUID,RoleId,Street,StreetAdditional,PostalCode,City,State,Country) values(?,?,?,?,?,?,?,?)";
			int insertaddress=jdbcTemplate.update(addresssql,uuid.toString(),1,org.getAddress().getStreet(),org.getAddress().getStreetAdditional(),org.getAddress().getPostalcode(),org.getAddress().getCity(),org.getAddress().getState(),org.getAddress().getCountry());
			
			
			if(insertprofile>0&&inserttenant>0&&insertlogin>00&&insertaddress>0)
			{
				emailService.sendMail_Register(org.getEmail(),randompassword);
				msg.setSuccessMessage("Registered Successfully.Please check your email before proceeding.");
				registerOrganization.setMessage(msg);
				registerOrganization.setOrguuid(uuid);
				registerOrganization.setStatus("Success");
			}
			}
			else
			{
				msg.setErrorMessage("No permission to create customers");
				registerOrganization.setMessage(msg);
			return registerOrganization;	
			}
			
			
			
			
			
		return registerOrganization;
		}
	}
	
	
	public Message updateById(Organization org)
	{
		
		Message msg=new Message();
		Address addr=new Address();
		
		String profilesql="Update Profile set FirstName=?,LastName=?,Phone=?,Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=?,Modified_On=? where TenantId is null and RootId=? and CompanyId is null and PhysicianId is null and patientId is null ";
		int updateProfile=jdbcTemplate.update(profilesql,org.getFirstName(),org.getLastName(),org.getPhone(),org.getAddress().getStreet(),org.getAddress().getStreetAdditional(),org.getAddress().getPostalcode(),org.getAddress().getCity(),org.getAddress().getState(),org.getAddress().getCountry(),timestamp.getTime(),org.getOrguuid());
		
		String rootsql="Update Root set Name=?,Phone=?,Type=?, Modified_On=? where RootId=?";
		int updateRoot=jdbcTemplate.update(rootsql,org.getFirstName(),org.getPhone(),org.getType(),timestamp.getTime(),org.getOrguuid());
		
		String addresssql="Update Address set Street=?,StreetAdditional=?,PostalCode=?,City=?,State=?,Country=? where UUID=?";
		 int updateaddress=jdbcTemplate.update(addresssql,org.getAddress().getStreet(),org.getAddress().getStreetAdditional(),org.getAddress().getPostalcode(),org.getAddress().getCity(),org.getAddress().getState(),org.getAddress().getCountry(),org.getOrguuid());
		 
		
		//int updateProfile=jdbcTemplate.update("Update Profile set Name='"+tenant.getFirstName()+"',Phone='"+tenant.getPhone()+"',Street='"+tenant.getAddress().getStreet()+"',Street2='"+tenant.getAddress().getStreetAdditional()+"',PostalCode='"+tenant.getAddress().getPostalcode()+"',City='"+tenant.getAddress().getCity()+"',Country='"+tenant.getAddress().getCountry()+"' where TenantId='"+tenant.getUuid()+"'");
		//int updateTenant=jdbcTemplate.update("Update Tenant set  Name='"+tenant.getFirstName()+"',Phone='"+tenant.getPhone()+"',Street='"+tenant.getAddress().getStreet()+"',Street2='"+tenant.getAddress().getStreetAdditional()+"',PostalCode='"+tenant.getAddress().getPostalcode()+"',City='"+tenant.getAddress().getCity()+"',Country='"+tenant.getAddress().getCountry()+"'where Id='"+tenant.getUuid()+"'");
		if(updateProfile>0&&updateRoot>0&&updateaddress>0)
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
		
		
		int deleteLogin=jdbcTemplate.update("Delete FROM Login WHERE UUID=? ", id);
	
	
		int deleteRoot=jdbcTemplate.update("Update Root set Status='inactive' where RootId=?",id);
	    
		
			
		int deleteProfile=jdbcTemplate.update("Update Profile set Status='inactive' where TenantId is null and RootId=? and CompanyId is null and PhysicianId is null and patientId is null",id);
	    
		
		if(deleteLogin>0&&deleteRoot>0&&deleteProfile>0)
		{
			msg.setSuccessMessage("Delete Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Delete Details");
		}
		return msg;
	}
	
	
	
	
	public String getRootType(String id)
	{
		
		String sql = "SELECT Type FROM Root WHERE RootId=? ";

	    String type = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return type;
		
	}
}
