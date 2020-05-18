package com.iThingsFoundation.IThingsFramework.Login;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.Message;

public class LoginDetails {
	
	private String uuid;
	private String firstName;
	private String lastName;
	private String phone;
	private String email ;
	private int roleId; 
	private String gender;
	private String dob;
	private Address address;
	private String status;
	private Message message;
		
	
	public LoginDetails() {
		super();
	}


	public LoginDetails(String uid, String firstName, String secondName, String phone, String email,
			int roleId, String gender, String dob, Address address, String status) {
		super();
		this.uuid = uid;

		this.firstName = firstName;
		this.lastName = secondName;
		this.phone = phone;
		this.email = email;
		this.roleId = roleId;
		this.gender = gender;
		this.dob = dob;
		this.address = address;
		this.status = status;
	}


	



	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	

	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getRoleId() {
		return roleId;
	}


	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getDob() {
		return dob;
	}


	public void setDob(String dob) {
		this.dob = dob;
	}


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Message getMessage() {
		return message;
	}


	public void setMessage(Message message) {
		this.message = message;
	}


	


}