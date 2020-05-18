package com.iThingsFoundation.IThingsFramework.Customer;

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
public class CustomerController {
	
	
	@Autowired
	  private CustomerService customerService;
	@Autowired
	private SessionService sessionService;
	
	@PostMapping("/getcustomerdetails")
	public CustomerList getCustomerDetails(@RequestBody Customer customer,HttpServletRequest request) throws JsonMappingException, JsonProcessingException 
	{
        String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    CustomerList list=new CustomerList();
		
	    if(sessionstatus!="Session Expired")
	      {
     
		return customerService.getCustomerDetails(customer.getBatchsize(),customer.getOffset(),customer);
	
	      
	      }   
	    else
	    {
	    	list.setStatus("Session Expired");
	    	return list;
	    }
	}
	
	@PostMapping("/registercustomer")
	public Customer registerCustomer(@RequestBody Customer registerDetails, HttpServletRequest request) {
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
	    Customer customer=new Customer();
			
			
			
			if(sessionstatus!="Session Expired")
		      {
	     
				return customerService.register(registerDetails);
		      }
		      else
		      {
		    	    msg.setErrorMessage("Session Expired");;
		    	    customer.setMessage(msg); 
		    	  return customer;
		      }
			
			
		}
	
	@PostMapping("/updatecustomer")
	public Message updateCustomer(@RequestBody Customer updateDetails, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return customerService.updateById(updateDetails);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }
	}
	
	@PostMapping("/deletecustomer")
	public Message deleteCustomer(@RequestBody Customer Details, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return customerService.deleteById(Details);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }


    }
}
