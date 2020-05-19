package com.iThingsFoundation.IThingsFramework.Organization;

import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.Message;

public class Organization {
	
	private Object orguuid;
	private Object rootId;
	private String firstName;
	private String lastName;
	private String phone;
	private String email ;
	private String roleId; 
	private String type;
	private Address address;
	private String status;
	private Message message;
	
	
	
	public Organization() {
		super();
	}


	public Organization(Object orguuid, Object rootId, String firstName, String lastName, String phone, String email,
			String roleId, String type, Address address, String status, Message message) {
		super();
		this.orguuid = orguuid;
		this.rootId = rootId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.roleId = roleId;
		this.type = type;
		this.address = address;
		this.status = status;
		this.message = message;
	}








	public Object getRootId() {
		return rootId;
	}


	public void setRootId(Object rootId) {
		this.rootId = rootId;
	}


	public Object getOrguuid() {
		return orguuid;
	}



	public void setOrguuid(Object orguuid) {
		this.orguuid = orguuid;
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



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
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
