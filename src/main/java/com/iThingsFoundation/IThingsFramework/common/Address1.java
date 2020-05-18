package com.iThingsFoundation.IThingsFramework.common;

public class Address1 {
	
	private String permanentAddress;
	private String temporaryAddress;
	
	
	
	public Address1() {
		super();
	}



	public Address1(String permanentAddress, String temporaryAddress) {
		super();
		this.permanentAddress = permanentAddress;
		this.temporaryAddress = temporaryAddress;
	}



	public String getPermanentAddress() {
		return permanentAddress;
	}



	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}



	public String getTemporaryAddress() {
		return temporaryAddress;
	}



	public void setTemporaryAddress(String temporaryAddress) {
		this.temporaryAddress = temporaryAddress;
	}



	
}
