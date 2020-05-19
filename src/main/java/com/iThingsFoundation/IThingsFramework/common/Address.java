package com.iThingsFoundation.IThingsFramework.common;

public class Address {
	private String street;
	private String streetAdditional;
	private String postalcode;
	private String city;
	private String state;
	private String country;
	
	
	
	public Address() {
		super();
	}


	public Address(String street, String streetAdditional, String postalcode, String city, String state, String country) {
		super();
		this.street = street;
		this.streetAdditional = streetAdditional;
		this.postalcode = postalcode;
		this.city = city;
		this.state = state;
		this.country = country;
	}
	
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetAdditional() {
		return streetAdditional;
	}
	public void setStreetAdditional(String streetAdditional) {
		this.streetAdditional = streetAdditional;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}
	
	
	

}
