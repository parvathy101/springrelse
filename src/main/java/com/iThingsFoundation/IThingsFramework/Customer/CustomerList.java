package com.iThingsFoundation.IThingsFramework.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerList {
	
	private String nextoffset;
	private String remainingRecords;
	private ArrayList<Customer> customers;
    private String status;
	
	
	
	public CustomerList() {
		super();
	}


	public CustomerList(String nextoffset,String remainingRecords, ArrayList<Customer> customers) {
		super();
		this.nextoffset = nextoffset;
		this.remainingRecords = remainingRecords;
		this.customers = customers;
	}


	


	public String getNextoffset() {
		return nextoffset;
	}


	public void setNextoffset(String nextoffset) {
		this.nextoffset = nextoffset;
	}


	public String getRemainingRecords() {
		return remainingRecords;
	}


	public void setRemainingRecords(String a) {
		this.remainingRecords = a;
	}


	public ArrayList<Customer> getCustomers() {
		return customers;
	}


	public void setCustomers(ArrayList<Customer> customers) {
		this.customers = customers;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
