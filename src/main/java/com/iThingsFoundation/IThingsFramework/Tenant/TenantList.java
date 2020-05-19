package com.iThingsFoundation.IThingsFramework.Tenant;

import java.util.ArrayList;

import com.iThingsFoundation.IThingsFramework.Company.Company;

public class TenantList {
	private String nextoffset;
	private String remainingRecords;
	private ArrayList<Tenant> tenants;
    private String status;
    
    
	public TenantList() {
		super();
	}


	public TenantList(String nextoffset, String remainingRecords, ArrayList<Tenant> tenants, String status) {
		super();
		this.nextoffset = nextoffset;
		this.remainingRecords = remainingRecords;
		this.tenants = tenants;
		this.status = status;
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


	public void setRemainingRecords(String remainingRecords) {
		this.remainingRecords = remainingRecords;
	}


	public ArrayList<Tenant> getTenants() {
		return tenants;
	}


	public void setTenants(ArrayList<Tenant> tenants) {
		this.tenants = tenants;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
    
    
    

}
