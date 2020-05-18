package com.iThingsFoundation.IThingsFramework.Patient;

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
public class PatientController {
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private SessionService sessionService;
	
	@PostMapping("/getpatientdetails")
	public PatientList PatientList (@RequestBody Patient patient,HttpServletRequest request)
	{
        String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    PatientList list=new PatientList();
		
	    if(sessionstatus!="Session Expired")
	      {
     
		return patientService.getPatientDetails(patient.getBatchsize(),patient.getOffset(),patient);
	
	      
	      }   
	    else
	    {
	    	list.setStatus("Session Expired");
	    	return list;
	    }
	}
	
	
	@PostMapping("/registerpatient")
	public Patient registerPatient(@RequestBody Patient registerDetails, HttpServletRequest request) {
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
	    Patient patient=new Patient();
			
			
			
			if(sessionstatus!="Session Expired")
		      {
	     
				return patientService.register(registerDetails);
		      }
		      else
		      {
		    	    msg.setErrorMessage("Session Expired");;
		    	    patient.setMessage(msg); 
		    	  return patient;
		      }
			
			
		}
	
	@PostMapping("/updatepatient")
	public Message updatePatient(@RequestBody Patient updateDetails, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return patientService.updateById(updateDetails);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }
	}
	
	
	@PostMapping("/deletepatient")
	public Message deletePatient(@RequestBody Patient details, HttpServletRequest request)
	{
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return patientService.deleteById(details);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }


    }
	

}
