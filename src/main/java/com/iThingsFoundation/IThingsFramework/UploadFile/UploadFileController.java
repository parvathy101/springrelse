package com.iThingsFoundation.IThingsFramework.UploadFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iThingsFoundation.IThingsFramework.Tenant.Tenant;
import com.iThingsFoundation.IThingsFramework.common.Message;
import com.iThingsFoundation.IThingsFramework.common.SessionService;



@RestController
public class UploadFileController{
	
	@Autowired
	private UploadFileService uploadService;
	
	@Autowired
	private SessionService sessionService;
	
	@PostMapping("/uploadphoto")
	public Message uploadPhoto(@RequestParam("file") MultipartFile file,@RequestParam("uuid") String uid,@RequestParam("roleId") String roleId, HttpServletRequest request) throws IOException, SQLException
	{
		
		//System.out.println(file.getOriginalFilename()+"----"+uid);
		String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return uploadService.uploadPhoto(file,uid,roleId);
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");;
				
	  	  return msg;
	    }
		
		
	}
	
	@PostMapping("/getuploadedphoto")
	private UploadFile getPhoto(@RequestBody UploadFile details, HttpServletRequest request) throws SQLException
	{
		
        String sessionid=request.getHeader("Authorization");
		
	    String sessionstatus=sessionService.SessionData(sessionid);
	    Message msg=new Message();
		UploadFile upload=new UploadFile();
		
		if(sessionstatus!="Session Expired")
	    {
	 
			return uploadService.getPhoto(details.getUuid(),details.getRoleId());
	    }
	    else
	    {
	  	    msg.setErrorMessage("Session Expired");
	  	    upload.setMessage(msg);
				
	  	  return upload;
	    }
		
	}

}
