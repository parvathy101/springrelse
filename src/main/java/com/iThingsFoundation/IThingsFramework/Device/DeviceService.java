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
		String customerId=null;
		String physicianId=null;
		String patientId=null;
		String lastpart=null;
        String exist=null;
        
        if(dev.getTenantId()!=null)
		{
        	exist=getTenantExist(dev.getTenantId().toString());
		}
	else if(dev.getCustomerId()!=null)
	{
		exist=getCustomerExist(dev.getCustomerId().toString());
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
	else if(dev.getCustomerId()!=null)
	{
		tenantId=getCustomerTenantId(dev.getCustomerId().toString());
		customerId=dev.getCustomerId().toString();
		lastpart="Status='active' and TenantId='"+tenantId+"' and CustomerId='"+customerId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
	}
	else if(dev.getPhysicianId()!=null)		
	{
		tenantId=getPhysicianTenantId(dev.getPhysicianId().toString());
		customerId=getPhysicianCustomerId(dev.getPhysicianId().toString());
		physicianId=dev.getPhysicianId().toString();
		lastpart="Status='active' and TenantId='"+tenantId+"' and CustomerId='"+customerId+"' and UserId='"+physicianId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
		
	}
	else if(dev.getPatientId()!=null)		
	{
		tenantId=getPatientTenantId(dev.getPatientId().toString());
		customerId=getPatientCustomerId(dev.getPatientId().toString());
		physicianId=getPatientPhysicianId(dev.getPatientId().toString());
		patientId=dev.getPatientId().toString();
		lastpart="Status='active' and TenantId='"+tenantId+"' and CustomerId='"+customerId+"' and UserId='"+physicianId+"' and PatientId='"+patientId+"' order by Id asc ) t limit "+offset+","+batchsize+"";
		
		
	}
		
		
		
		
		String sql="select t.* from (select @rownum:=@rownum+1 rownumber, t.* from Device t cross join (SELECT @rownum:=0) r where ";
		
		
		String sql2="SELECT count(Id) FROM Device where ";
		
		 if(dev.getFilter()!=null)
		 {
			 //String sql3;
			      if(dev.getFilter().getDeviceId()!=null)
			      {
			    	  sql+= "DeviceId like '"+dev.getFilter().getDeviceId()+"%' and "; 
			    	  sql2+= "DeviceId like '"+dev.getFilter().getDeviceId()+"%' and ";
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
			 device.setDeviceuuid(row.get("DeviceUUID"));
			 device.setDeviceId((String)row.get("DeviceId"));
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
				sqlnew = sql2+" Id>"+id+" and TenantId='"+tenantId+"'";
			}
		else if(dev.getCustomerId()!=null)
		{
			tenantId=getCustomerTenantId(dev.getCustomerId().toString());
			customerId=dev.getCustomerId().toString();
			sqlnew = sql2+" Id>"+id+" and TenantId='"+tenantId+"' and CustomerId='"+customerId+"'";
		}
		else if(dev.getPhysicianId()!=null)		
		{
			tenantId=getPhysicianTenantId(dev.getPhysicianId().toString());
			customerId=getPhysicianCustomerId(dev.getPhysicianId().toString());
			physicianId=dev.getPhysicianId().toString();
			sqlnew = sql2+" Id>"+id+" and TenantId='"+tenantId+"' and CustomerId='"+customerId+"' and UserId='"+physicianId+"'";
		}
		else if(dev.getPatientId()!=null)		
		{
			tenantId=getPatientTenantId(dev.getPatientId().toString());
			customerId=getPatientCustomerId(dev.getPatientId().toString());
			physicianId=getPatientPhysicianId(dev.getPatientId().toString());
			patientId=dev.getPatientId().toString();
			sqlnew = sql2+" Id>"+id+" and TenantId='"+tenantId+"' and CustomerId='"+customerId+"' and UserId='"+physicianId+"' and PatientId='"+patientId+"'";

			
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
		String tenantId = null;
		String customerId=null;
		String physicianId=null;
		String patientId=null;
		
		 if(device.getTenantId()!=null)
			{
				tenantId=device.getTenantId().toString();
				
			}
		else if(device.getCustomerId()!=null)
		{
			tenantId=getCustomerTenantId(device.getCustomerId().toString());
			customerId=device.getCustomerId().toString();
		}
		else if(device.getPhysicianId()!=null)		
		{
			tenantId=getPhysicianTenantId(device.getPhysicianId().toString());
			customerId=getPhysicianCustomerId(device.getPhysicianId().toString());
			physicianId=device.getPhysicianId().toString();
			
		}
		else if(device.getPatientId()!=null)		
		{
			tenantId=getPatientTenantId(device.getPatientId().toString());
			customerId=getPatientCustomerId(device.getPatientId().toString());
			physicianId=getPatientPhysicianId(device.getPatientId().toString());
			patientId=device.getPatientId().toString();
			
			
		}
		
		
		
		String sql="select * from Device where DeviceId=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,device.getDeviceId());
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
		
			String devicesql="Insert into Device(DeviceId,DeviceUUID,Status,Created_On,FirmwareVersion,ModelNo,TenantId,CustomerId,UserId,PatientId) values (?,?,?,?,?,?,?,?,?,?)";
			int insertdevice=jdbcTemplate.update(devicesql,device.getDeviceId(),uuid.toString(),"active",timestamp.getTime(),device.getFirmwareVersion(),device.getModelNo(),tenantId,customerId,physicianId,patientId);
			
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
		String devicesql="Update Device set FirmwareVersion=?,ModelNo=?,Modified_On=? where DeviceUUID=?";
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
        String devicesql="Update Device set Status='inactive' where DeviceUUID=?";
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
	
	public String getCustomerTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM Customer WHERE CustomerUUId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPhysicianTenantId(String id)
	{
		
		String sql = "SELECT TenantId FROM User WHERE UserId=? ";

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
	public String getPhysicianCustomerId(String id)
	{
		
		String sql = "SELECT CustomerUUId FROM User WHERE UserId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPatientCustomerId(String id)
	{
		
		String sql = "SELECT CustomerUUId FROM Patient WHERE PatientId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getPatientPhysicianId(String id)
	{
		
		String sql = "SELECT UserId FROM Patient WHERE PatientId=? ";

	    String uid = (String) jdbcTemplate.queryForObject(
	            sql, new Object[] { id }, String.class);

	    return uid;
		
	}
	
	public String getCustomerExist(String id)
	{
		
		String sql = "SELECT * FROM Device WHERE CustomerId=? and Status='active' ";
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
		
		String sql = "SELECT * FROM Device WHERE UserId=? and Status='active' ";
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
	


}
