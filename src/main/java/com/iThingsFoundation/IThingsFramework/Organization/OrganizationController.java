package com.iThingsFoundation.IThingsFramework.Organization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iThingsFoundation.IThingsFramework.Tenant.Tenant;
import com.iThingsFoundation.IThingsFramework.common.Message;
import com.iThingsFoundation.IThingsFramework.common.SessionService;

@RestController
public class OrganizationController {
	
	@Autowired
	  private OrganizationService organizationService;
	
	@Autowired
	private SessionService sessionService;
	
	
	@PostMapping("/registerorganization")
	public Organization registerCustomer(@RequestBody Organization registerDetails, HttpServletRequest request) {
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
	    Organization org=new Organization();
			
			
			
			if(sessionstatus!="Session Expired")
		      {
	     
				return organizationService.register(registerDetails);
		      }
		      else
		      {
		    	    msg.setErrorMessage("Session Expired");;
		    	    org.setMessage(msg); 
		    	  return org;
		      }
			
			
		}

	
	@PostMapping("/updateorganization")
	public Message updateCustomer(@RequestBody Organization updateDetails, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return organizationService.updateById(updateDetails);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }
	}
	
	@PostMapping("/deleteorganization")
	public Message deleteTenant(@RequestBody Organization details, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return organizationService.deleteById(details.getOrguuid());
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }
		
		
	}
}
