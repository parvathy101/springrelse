package com.iThingsFoundation.IThingsFramework.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender mailSender;
      
    public void sendMail_Register(String to,String pass)
    {
    	String subject="Please verify your email";
    	String body="Thank you for registering . Please use the below password to login to the system.";
    	body+=System.getProperty("line.separator"); 
    	body+="Email: "+to+"";
    	body+=System.getProperty("line.separator"); 
    	body+=" password: "+pass+"";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("RegisterService@mailinator.com");
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}
