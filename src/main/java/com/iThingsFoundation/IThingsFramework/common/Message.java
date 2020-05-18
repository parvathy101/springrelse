package com.iThingsFoundation.IThingsFramework.common;

public class Message {
	
	
	private String successMessage;
	private String errorMessage;
	
	public Message() {
		super();
	}

	public Message(String successMessage, String errorMessage) {
		
		this.successMessage = successMessage;
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
	

}

