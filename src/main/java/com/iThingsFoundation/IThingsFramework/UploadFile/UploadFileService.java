package com.iThingsFoundation.IThingsFramework.UploadFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iThingsFoundation.IThingsFramework.common.Message;



@Service
public class UploadFileService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	
	
	public Message uploadPhoto(MultipartFile file,String uuid,String roleId) throws IOException, SQLException
	{
		Message msg=new Message();
		System.out.printf("File name=%s, size=%s\n",file.getOriginalFilename(),file.getSize());
		//System.out.printf("Text--"+menuText);
		String dbname = jdbcTemplate.getDataSource().getConnection().getCatalog();
		String tenantId=null;
		String companyId=null;
		String physicianId=null;
		String patientId=null;
		String rootId=null;
	      System.out.println(uuid+"---"+roleId);
		  byte[] bytes;
		  
		    bytes = file.getBytes();
		    Path path = Paths.get("../../SpringBootImages/").resolve(uuid+".jpg");// local storage
			//Path path = Paths.get("../../var/www/html/"+dbname+"/photos/").resolve(uuid+".jpg");//178
			String profilesql=null;
			if(roleId.equals("1"))
		    {
		    	
		    	profilesql="update Profile set Photo_Name=? where RootId=? and TenantId is null and CompanyId is null and PatientId is null and PhysicianId is null";
		    }
		    if(roleId.equals("2")||roleId.equals("3"))
		    {
		    	rootId=getTenantRootId(uuid);
		    	profilesql="update Profile set Photo_Name=? where RootId='"+rootId+"' and TenantId=? and CompanyId is null and PatientId is null and PhysicianId is null";
		    }
		    else if(roleId.equals("4")||roleId.equals("5"))
		    {
		    	tenantId=getCompanyTenantId(uuid);
		    	rootId=getTenantRootId(tenantId);
		    	profilesql="update Profile set Photo_Name=? where RootId='"+rootId+"' and TenantId='"+tenantId+"' and CompanyId=? and PatientId is null and PhysicianId is null";
		    	
		    }
		    else if(roleId.equals("6")||roleId.equals("7"))
		    {
		    	tenantId=getPhysicianTenantId(uuid);
		    	rootId=getTenantRootId(tenantId);
		    	companyId=getPhysicianCompanyId(uuid);
		    	profilesql="update Profile set Photo_Name=? where RootId='"+rootId+"' and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' and PatientId is null and PhysicianId=?";
		    	
		    }
		    else if(roleId.equals("8"))
		    {
		    	tenantId=getPatientTenantId(uuid);
		    	rootId=getTenantRootId(tenantId);
		    	companyId=getPatientCompanyId(uuid);
		    	physicianId=getPatientPhysicianId(uuid);
		    	profilesql="update Profile set Photo_Name=? where RootId='"+rootId+"' and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' and PatientId=? and PhysicianId='"+physicianId+"'";
		    }
			
			int count=jdbcTemplate.update(profilesql,uuid+".jpg",uuid);
		
			//Files.write(path, bytes);
			// Path targetLocation = this.fileStorageLocation.resolve(fileName);
	         long read=   Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		
	         if(count>0&&read>0)
			{

		    
		
			msg.setSuccessMessage("Upload Photo Successfully.");
			
			}
	         else
	         {
	        	msg.setErrorMessage("not upload successfully"); 
	         }
		
			
		return msg;
	}
	
	
	public UploadFile getPhoto(String uuid,String roleId) throws SQLException
	{
		UploadFile upload=new UploadFile();
		Message msg=new Message();
		String dbname = jdbcTemplate.getDataSource().getConnection().getCatalog();
		//String imagepath="http://178.128.165.237/"+dbname+"/photos/"; //178
		String imagepath="/home/iorbitdeveloper/Documents/SpringBootImages/";
		
		String tenantId=null;
		String companyId=null;
		String physicianId=null;
		String patientId=null;
		String rootId=null;
		String profilesql=null;
		if(roleId.equals("1"))
	    {
	    	
	    	profilesql="update Profile set Photo_Name=? where RootId=? and TenantId is null and CompanyId is null and PatientId is null and PhysicianId is null";
	    }
	    if(roleId.equals("2")||roleId.equals("3"))
	    {
	    	rootId=getTenantRootId(uuid);
	    	profilesql="update Profile set Photo_Name=? where RootId='"+rootId+"' and TenantId=? and CompanyId is null and PatientId is null and PhysicianId is null";
	    }
	    else if(roleId.equals("4")||roleId.equals("5"))
	    {
	    	tenantId=getCompanyTenantId(uuid);
	    	rootId=getTenantRootId(tenantId);
	    	profilesql="update Profile set Photo_Name=? where RootId='"+rootId+"' and TenantId='"+tenantId+"' and CompanyId=? and PatientId is null and PhysicianId is null";
	    	
	    }
	    else if(roleId.equals("6")||roleId.equals("7"))
	    {
	    	tenantId=getPhysicianTenantId(uuid);
	    	rootId=getTenantRootId(tenantId);
	    	companyId=getPhysicianCompanyId(uuid);
	    	profilesql="update Profile set Photo_Name=? where RootId='"+rootId+"' and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' and PatientId is null and PhysicianId=?";
	    	
	    }
	    else if(roleId.equals("8"))
	    {
	    	tenantId=getPatientTenantId(uuid);
	    	rootId=getTenantRootId(tenantId);
	    	companyId=getPatientCompanyId(uuid);
	    	physicianId=getPatientPhysicianId(uuid);
	    	profilesql="update Profile set Photo_Name=? where RootId='"+rootId+"' and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' and PatientId=? and PhysicianId='"+physicianId+"'";
	    }
		
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(profilesql,uuid);
		for (Map<String, Object> row : rows) 
        {
			
			String name=(String)row.get("Photo_Name");
			Path path = Paths.get("../../SpringBootImages/").resolve(name);
			//Path path = Paths.get("../../var/www/html/"+dbname+"/photos/").resolve(name);//178
			
			if(Files.exists(path))
					{
		    imagepath=imagepath+(String)row.get("Photo_Name");
		  upload.setFilePath(imagepath);
		  msg.setSuccessMessage("success");
		  upload.setMessage(msg);
		  
					}
			else
			{
				msg.setErrorMessage("image not found");
				  upload.setMessage(msg);
			}
        }
		
		return upload;
	}
	
	public String getCompanyTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Profile WHERE CompanyId=? limit 1 ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPhysicianTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Profile WHERE PhysicianId=? limit 1";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPatientTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Profile WHERE PatientId=? limit 1";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	public String getPhysicianCompanyId(String id)
	{
		
		String sql = "SELECT CompanyId FROM Profile WHERE PhysicianId=? limit 1";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
	}
	public String getPatientCompanyId(String id)
	{
		
		String sql = "SELECT CompanyId FROM Profile WHERE PatientId=? limit 1";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
	}
	
	public String getPatientPhysicianId(String id)
	{
		
		String sql = "SELECT PhysicianId FROM Profile WHERE PatientId=? limit 1";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
	}
	
	public String getTenantRootId(String id)
	{
		
		String sql = "SELECT RootId FROM Tenant WHERE TenantId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
}
