package com.iThingsFoundation.IThingsFramework.Tenant;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iThingsFoundation.IThingsFramework.Company.Company;
import com.iThingsFoundation.IThingsFramework.Company.CompanyList;
import com.iThingsFoundation.IThingsFramework.common.Message;
import com.iThingsFoundation.IThingsFramework.common.SessionService;





@RestController
public class TenantController {
	
	@Autowired
	  private TenantService tenantService;
	
	@Autowired
	private SessionService sessionService;
	
	@PostMapping("/gettenantdetails")
	public TenantList getCompanyDetails(@RequestBody Tenant tenant,HttpServletRequest request) throws JsonMappingException, JsonProcessingException 
	{
        String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    TenantList list=new TenantList();
		
	    if(sessionstatus!="Session Expired")
	      {
     
		return tenantService.getTenantDetails(tenant.getBatchsize(),tenant.getOffset(),tenant);
	
	      
	      }   
	    else
	    {
	    	list.setStatus("Session Expired");
	    	return list;
	    }
	}
	
@PostMapping("/registertenant")
public Tenant registerTenant(@RequestBody Tenant registerDetails, HttpServletRequest request) {
	String sessionid=request.getHeader("Authorization");
	
    String sessionstatus=sessionService.SessionData(sessionid);
    Message msg=new Message();
    Tenant tenant=new Tenant();
		
		
		
		if(sessionstatus!="Session Expired")
	      {
     
			return tenantService.register(registerDetails);
	      }
	      else
	      {
	    	    msg.setErrorMessage("Session Expired");;
	    	    tenant.setMessage(msg); 
	    	  return tenant;
	      }
		
		
	}

@PostMapping("/deletetenant")
public Message deleteTenant(@RequestBody Tenant details, HttpServletRequest request)
{
	String sessionid=request.getHeader("Authorization");
	
    String sessionstatus=sessionService.SessionData(sessionid);
    Message msg=new Message();
	
	
	if(sessionstatus!="Session Expired")
    {
 
		return tenantService.deleteById(details.getUuid());
    }
    else
    {
  	    msg.setErrorMessage("Session Expired");;
			
  	  return msg;
    }
	
	
}

@PostMapping("/updatetenant")
public Message updateTenant(@RequestBody Tenant updateDetails, HttpServletRequest request)
{
	String sessionid=request.getHeader("Authorization");
	
    String sessionstatus=sessionService.SessionData(sessionid);
    Message msg=new Message();
	
	
	
	if(sessionstatus!="Session Expired")
    {
 
		return tenantService.updateById(updateDetails);
    }
    else
    {
  	    msg.setErrorMessage("Session Expired");;
			
  	  return msg;
    }
}




}
