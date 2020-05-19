package com.iThingsFoundation.IThingsFramework.Device;

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
import com.iThingsFoundation.IThingsFramework.Physician.Physician;
import com.iThingsFoundation.IThingsFramework.Physician.PhysicianList;
import com.iThingsFoundation.IThingsFramework.common.Address1;
import com.iThingsFoundation.IThingsFramework.common.Filters;
import com.iThingsFoundation.IThingsFramework.common.Message;

@Service
public class DeviceService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	UUID uuid;
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	
	
	public DeviceList getDeviceDetails(String batchsize,String offset,Device dev)
	{
		ArrayList<Device> devices=new ArrayList<>();
		DeviceList list=new DeviceList();
		Address1 address=new Address1();
		String remainingRecords=null;
		String lastRowno=null;
		int id=0;
		String tenantId=null;
		String companyId=null;
		String physicianId=null;
		String patientId=null;
		String lastpart=null;
		String rootId=null;
        String exist=null;
        
        if(dev.getTenantId()!=null)
		{
        	exist=getTenantExist(dev.getTenantId().toString());
		}
	else if(dev.getCompanyId()!=null)
	{
		exist=getCompanyExist(dev.getCompanyId().toString());
	}
	else if(dev.getPhysicianId()!=null)		
	{
		 exist=getPhysicianExist(dev.getPhysicianId().toString());
	}
	else if(dev.getPatientId()!=null)		
	{
		exist=getPatientExist(dev.getPatientId().toString());
	}
        
        if(exist=="yes")
		{
		
       if(dev.getTenantId()!=null)
		{
			tenantId=dev.getTenantId().toString();
			lastpart="Status='active' and TenantId='"+tenantId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
		}
	else if(dev.getCompanyId()!=null)
	{
		tenantId=getCompanyTenantId(dev.getCompanyId().toString());
		companyId=dev.getCompanyId().toString();
		lastpart="Status='active' and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
	}
	else if(dev.getPhysicianId()!=null)		
	{
		tenantId=getPhysicianTenantId(dev.getPhysicianId().toString());
		companyId=getPhysicianCompanyId(dev.getPhysicianId().toString());
		physicianId=dev.getPhysicianId().toString();
		lastpart="Status='active' and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' and PhysicianId='"+physicianId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
		
	}
	else if(dev.getPatientId()!=null)		
	{
		tenantId=getPatientTenantId(dev.getPatientId().toString());
		companyId=getPatientCompanyId(dev.getPatientId().toString());
		physicianId=getPatientPhysicianId(dev.getPatientId().toString());
		patientId=dev.getPatientId().toString();
		lastpart="Status='active' and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' and PhysicianId='"+physicianId+"' and PatientId='"+patientId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
		
		
	}
		
		
		
		
		String sql="select t.* from (select @rownum:=@rownum+1 rownumber, t.* from Device t cross join (SELECT @rownum:=0) r where ";
		
		
		String sql2="SELECT count(Id) FROM Device where ";
		
		 if(dev.getFilter()!=null)
		 {
		
			 //String sql3;
			      if(dev.getFilter().getDeviceNumber()!=null)
			      {
			    	  sql+= "DeviceNumber like '"+dev.getFilter().getDeviceNumber()+"%' and "; 
			    	  sql2+= "DeviceNumber like '"+dev.getFilter().getDeviceNumber()+"%' and ";
			      }
			      if(dev.getFilter().getFirmwareVersion()!=null)
			      {
			    	  sql+= "FirmwareVersion like '"+dev.getFilter().getFirmwareVersion()+"%' and "; 
			    	  sql2+= "FirmwareVersion like '"+dev.getFilter().getFirmwareVersion()+"%' and "; 
			    	  
			      }
			      if(dev.getFilter().getModelNo()!=null)
			      {
			    	  sql+= "ModelNo like '"+dev.getFilter().getModelNo()+"%' and "; 
			    	  sql2+= "ModelNo like '"+dev.getFilter().getModelNo()+"%' and "; 
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
			 Device device=new Device();
			 device.setDeviceuuid(row.get("DeviceId"));
			 device.setDeviceNumber((String)row.get("DeviceNumber"));
			 device.setFirmwareVersion((String)row.get("FirmwareVersion"));
			 device.setModelNo((String)row.get("ModelNo"));
			 double val=(double)row.get("rownumber");
			  int rowno=(int) Math.round(val);
			   lastRowno=Integer.toString(rowno);
			   
			  
			   
			   id=(int) row.get("Id");
			      
			   devices.add(device);
	        }
		 
		 
		 String sqlnew = null;
		     
		    
		    if(dev.getTenantId()!=null)
			{
				tenantId=dev.getTenantId().toString();
				sqlnew = sql2+" Id>"+id+" and TenantId='"+tenantId+"' and Status='active'";
			}
		else if(dev.getCompanyId()!=null)
		{
			tenantId=getCompanyTenantId(dev.getCompanyId().toString());
			companyId=dev.getCompanyId().toString();
			sqlnew = sql2+" Id>"+id+" and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' and Status='active'";
		}
		else if(dev.getPhysicianId()!=null)		
		{
			tenantId=getPhysicianTenantId(dev.getPhysicianId().toString());
			companyId=getPhysicianCompanyId(dev.getPhysicianId().toString());
			physicianId=dev.getPhysicianId().toString();
			sqlnew = sql2+" Id>"+id+" and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' and PhysicianId='"+physicianId+"' and Status='active'";
		}
		else if(dev.getPatientId()!=null)		
		{
			tenantId=getPatientTenantId(dev.getPatientId().toString());
			companyId=getPatientCompanyId(dev.getPatientId().toString());
			physicianId=getPatientPhysicianId(dev.getPatientId().toString());
			patientId=dev.getPatientId().toString();
			sqlnew = sql2+" Id>"+id+" and TenantId='"+tenantId+"' and CompanyId='"+companyId+"' and PhysicianId='"+physicianId+"' and PatientId='"+patientId+"'";

			
		}
			
		    
            
		     remainingRecords = (String) jdbcTemplate.queryForObject(sqlnew, String.class);
		     
		       list.setNextoffset(lastRowno);
		       list.setRemainingRecords(remainingRecords);
		      list.setDevice(devices);
		      list.setStatus("Success");
		}
		else
		{
			list.setStatus("Not Found");
		}   
		return list;
	}
	
	
	public Device register(Device device)
	{
		uuid= Generators.timeBasedGenerator().generate();
		Device registerDevice=new Device();
		Message msg=new Message();
		String rootId=null;
		String tenantId = null;
		String companyId=null;
		String physicianId=null;
		String patientId=null;
		
		 if(device.getTenantId()!=null)
			{
				tenantId=device.getTenantId().toString();
				rootId=getTenantRootId(tenantId);
				
			}
		else if(device.getCompanyId()!=null)
		{
			tenantId=getCompanyTenantId(device.getCompanyId().toString());
			rootId=getTenantRootId(tenantId);
			companyId=device.getCompanyId().toString();
		}
		else if(device.getPhysicianId()!=null)		
		{
			tenantId=getPhysicianTenantId(device.getPhysicianId().toString());
			companyId=getPhysicianCompanyId(device.getPhysicianId().toString());
			physicianId=device.getPhysicianId().toString();
			rootId=getTenantRootId(tenantId);
			
		}
		else if(device.getPatientId()!=null)		
		{
			tenantId=getPatientTenantId(device.getPatientId().toString());
			companyId=getPatientCompanyId(device.getPatientId().toString());
			physicianId=getPatientPhysicianId(device.getPatientId().toString());
			patientId=device.getPatientId().toString();
			rootId=getTenantRootId(tenantId);
			
			
		}
		
		
		
		String sql="select * from Device where DeviceNumber=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,device.getDeviceNumber());
		if(rows.size()>0)
		{
		
			msg.setErrorMessage("Already registered");
			registerDevice.setMessage(msg);
		return registerDevice;	
		}
		else
		{
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			String generatedString = RandomStringUtils.random(10,characters);
		
			String devicesql="Insert into Device(DeviceId,DeviceNumber,Status,Created_On,FirmwareVersion,ModelNo,RootId,TenantId,CompanyId,PhysicianId,PatientId) values (?,?,?,?,?,?,?,?,?,?,?)";
			int insertdevice=jdbcTemplate.update(devicesql,uuid.toString(),device.getDeviceNumber(),"active",timestamp.getTime(),device.getFirmwareVersion(),device.getModelNo(),rootId,tenantId,companyId,physicianId,patientId);
			
			if(insertdevice>0)
			{
				msg.setSuccessMessage("Registered Successfully ");
				registerDevice.setMessage(msg);
				registerDevice.setDeviceuuid(uuid);
			
			}
			
			
			
						
		return registerDevice;
		}
	}
	
	
	public Message updateById(Device device)
	{
		
		Message msg=new Message();
		String devicesql="Update Device set FirmwareVersion=?,ModelNo=?,Modified_On=? where DeviceId=?";
		int updatedevice=jdbcTemplate.update(devicesql,device.getFirmwareVersion(),device.getModelNo(),timestamp.getTime(),device.getDeviceuuid());
		if(updatedevice>0)
		{
			msg.setSuccessMessage("Update Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Update Details");
		}
		return msg;
	}
	
	public Message deleteById(Device device)
	{
		Message msg=new Message();
        String devicesql="Update Device set Status='inactive' where DeviceId=?";
		int deletedevice=jdbcTemplate.update(devicesql,device.getDeviceuuid());
		if(deletedevice>0)
		{
			msg.setSuccessMessage("Delete Details Successfully");
		}
		else {
			msg.setErrorMessage("Cant Delete Details");
		}
		return msg;
	}
	
	public String getCompanyTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Company WHERE CompanyId=? ";

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
	public String getPatientTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Patient WHERE PatientId=? ";

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
	
	public String getPatientCompanyId(String id)
	{
		
		String sql = "SELECT CompanyId FROM Patient WHERE PatientId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPatientPhysicianId(String id)
	{
		
		String sql = "SELECT PhysicianId FROM Patient WHERE PatientId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getCompanyExist(String id)
	{
		
		String sql = "SELECT * FROM Device WHERE CompanyId=? and Status='active' ";
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
		
		String sql = "SELECT * FROM Device WHERE PhysicianId=? and Status='active' ";
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
	
	public String getTenantExist(String id)
	{
		
		String sql = "SELECT * FROM Device WHERE TenantId=? and Status='active' ";
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
	
	public String getPatientExist(String id)
	{
		
		String sql = "SELECT * FROM Device WHERE PatientId=? and Status='active' ";
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
	public String getRootExist(String id)
	{
		
		String sql = "SELECT * FROM Device WHERE RootId=? and Status='active' ";
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

}
