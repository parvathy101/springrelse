package com.iThingsFoundation.IThingsFramework.Patient;

import com.iThingsFoundation.IThingsFramework.common.Address1;
import com.iThingsFoundation.IThingsFramework.common.Address;
import com.iThingsFoundation.IThingsFramework.common.Filters;
import com.iThingsFoundation.IThingsFramework.common.Message;

public class Patient {
	
	private Object patientuuid;
	private Object companyId;
	private Object physicianId;
	private String firstName;
	private String lastName;
	private String phone;
	private String email ;
	private String roleId;
	private String gender;
	private String dob;
	private Address address;
	private Message message;
	private String batchsize;
	private String offset;
	private Filters filter;
	
	
	
	public Patient() {
		super();
	}



	public Patient(Object patientuuid, Object ccompanyId, Object physicianId, String firstName, String lastName,
			String phone, String email, String roleId, String gender, String dob, Address address, Message message,
			String batchsize, String offset, Filters filter) {
		super();
		this.patientuuid = patientuuid;
		this.companyId = companyId;
		this.physicianId = physicianId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.roleId = roleId;
		this.gender = gender;
		this.dob = dob;
		this.address = address;
		this.message = message;
		this.batchsize = batchsize;
		this.offset = offset;
		this.filter = filter;
	}



	public Object getPatientuuid() {
		return patientuuid;
	}



	public void setPatientuuid(Object patientuuid) {
		this.patientuuid = patientuuid;
	}



	public Object getCompanyId() {
		return companyId;
	}



	public void setCompanyId(Object companyId) {
		this.companyId = companyId;
	}



	public Object getPhysicianId() {
		return physicianId;
	}



	public void setPhysicianId(Object physicianId) {
		this.physicianId = physicianId;
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



	public Message getMessage() {
		return message;
	}



	public void setMessage(Message message) {
		this.message = message;
	}



	public String getBatchsize() {
		return batchsize;
	}



	public void setBatchsize(String batchsize) {
		this.batchsize = batchsize;
	}



	public String getOffset() {
		return offset;
	}



	public void setOffset(String offset) {
		this.offset = offset;
	}



	public Filters getFilter() {
		return filter;
	}



	public void setFilter(Filters filter) {
		this.filter = filter;
	}



	

}
