package com.iThingsFoundation.IThingsFramework.Physician;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iThingsFoundation.IThingsFramework.Customer.Customer;
import com.iThingsFoundation.IThingsFramework.common.Message;
import com.iThingsFoundation.IThingsFramework.common.SessionService;


@RestController
public class PhysicianController {

	
	@Autowired
	private PhysicianService physicianService;
	
	@Autowired
	private SessionService sessionService;
	
	@PostMapping("/getphysiciandetails")
	public PhysicianList getPhysicianDetails(@RequestBody Physician physician,HttpServletRequest request)
	{
        String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    PhysicianList list=new PhysicianList();
		
	    if(sessionstatus!="Session Expired")
	      {
     
		return physicianService.getPhysicianDetails(physician.getBatchsize(),physician.getOffset(),physician);
	
	      
	      }   
	    else
	    {
	    	list.setStatus("Session Expired");
	    	return list;
	    }
	}
	
	
	@PostMapping("/registerphysician")
	public Physician registerPhysician(@RequestBody Physician registerDetails, HttpServletRequest request) {
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
	    Physician physician=new Physician();
			
			
			
			if(sessionstatus!="Session Expired")
		      {
	     
				return physicianService.register(registerDetails);
		      }
		      else
		      {
		    	    msg.setErrorMessage("Session Expired");;
		    	    physician.setMessage(msg); 
		    	  return physician;
		      }
			
			
		}
	
	
	@PostMapping("/updatephysician")
	public Message updatePhysician(@RequestBody Physician updateDetails, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return physicianService.updateById(updateDetails);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }
	}
	
	@PostMapping("/deletephysician")
	public Message deletePhysician(@RequestBody Physician details, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return physicianService.deleteById(details);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }


    }
	
	
}
