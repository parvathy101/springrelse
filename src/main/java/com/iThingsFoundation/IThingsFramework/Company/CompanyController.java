package com.iThingsFoundation.IThingsFramework.Company;

import java.util.List;

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
import com.iThingsFoundation.IThingsFramework.Tenant.Tenant;
import com.iThingsFoundation.IThingsFramework.common.Message;
import com.iThingsFoundation.IThingsFramework.common.SessionService;



@RestController
public class CompanyController {
	
	
	@Autowired
	  private CompanyService companyService;
	@Autowired
	private SessionService sessionService;
	
	@PostMapping("/getcompanydetails")
	public CompanyList getCompanyDetails(@RequestBody Company company,HttpServletRequest request) throws JsonMappingException, JsonProcessingException 
	{
        String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    CompanyList list=new CompanyList();
		
	    if(sessionstatus!="Session Expired")
	      {
     
		return companyService.getCompanyDetails(company.getBatchsize(),company.getOffset(),company);
	
	      
	      }   
	    else
	    {
	    	list.setStatus("Session Expired");
	    	return list;
	    }
	}
	
	@PostMapping("/registercompany")
	public Company registerCompany(@RequestBody Company registerDetails, HttpServletRequest request) {
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
	    Company company=new Company();
			
			
			
			if(sessionstatus!="Session Expired")
		      {
	     
				return companyService.register(registerDetails);
		      }
		      else
		      {
		    	    msg.setErrorMessage("Session Expired");;
		    	    company.setMessage(msg); 
		    	  return company;
		      }
			
			
		}
	
	@PostMapping("/updatecompany")
	public Message updateCompany(@RequestBody Company updateDetails, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return companyService.updateById(updateDetails);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }
	}
	
	@PostMapping("/deletecompany")
	public Message deleteCompany(@RequestBody Company Details, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return companyService.deleteById(Details);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }


    }
}
