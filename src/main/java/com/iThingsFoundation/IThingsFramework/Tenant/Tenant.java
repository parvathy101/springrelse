package com.iThingsFoundation.IThingsFramework.Tenant;



import java.util.UUID;

import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.Message;

public class Tenant {
	
	
	private Object uuid;
	private String firstName;
	private String lastName;
	private String phone;
	private String email ;
	private String roleId; 
	private String gender;
	private String dob;
	private Address address;
	private String status;
	private Message message;
	
	public Tenant() {
		super();
	}

	public Tenant(Object uid, String firstName, String lastName, String phone, String email, String roleId,
			String gender, String dob, Address address, String status, Message message) {
		super();
		this.uuid = uid;
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.roleId = roleId;
		this.gender = gender;
		this.dob = dob;
		this.address = address;
		this.status = status;
		this.message = message;
	}

	

	public Object getUuid() {
		return uuid;
	}

	public void setUuid(Object uuid2) {
		this.uuid = uuid2;
	}



	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
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
