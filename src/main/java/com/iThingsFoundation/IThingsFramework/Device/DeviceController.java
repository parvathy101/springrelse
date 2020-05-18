package com.iThingsFoundation.IThingsFramework.Device;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iThingsFoundation.IThingsFramework.Physician.Physician;
import com.iThingsFoundation.IThingsFramework.Physician.PhysicianList;
import com.iThingsFoundation.IThingsFramework.common.Message;
import com.iThingsFoundation.IThingsFramework.common.SessionService;


@RestController
public class DeviceController {
	
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private SessionService sessionService;
	
	@PostMapping("/getdevicedetails")
	public DeviceList getDeviceDetails(@RequestBody Device device,HttpServletRequest request)
	{
        String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    DeviceList list=new DeviceList();
		
	    if(sessionstatus!="Session Expired")
	      {
     
		return deviceService.getDeviceDetails(device.getBatchsize(),device.getOffset(),device);
	
	      
	      }   
	    else
	    {
	    	list.setStatus("Session Expired");
	    	return list;
	    }
	}
	
	@PostMapping("/registerdevice")
	public Device registerDevice(@RequestBody Device registerDetails, HttpServletRequest request) {
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
	    Device device=new Device();
			
			
			
			if(sessionstatus!="Session Expired")
		      {
	     
				return deviceService.register(registerDetails);
		      }
		      else
		      {
		    	    msg.setErrorMessage("Session Expired");;
		    	    device.setMessage(msg); 
		    	  return device;
		      }
			
			
		}
	
	@PostMapping("/updatedevice")
	public Message updateDevice(@RequestBody Device updateDetails, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return deviceService.updateById(updateDetails);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }
	}
	
	@PostMapping("/deletedevice")
	public Message deleteDevice(@RequestBody Device details, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return deviceService.deleteById(details);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }


    }
	

}
