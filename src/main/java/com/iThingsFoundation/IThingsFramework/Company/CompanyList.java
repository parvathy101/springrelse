package com.iThingsFoundation.IThingsFramework.Company;

import java.util.ArrayList;
import java.util.List;

public class CompanyList {
	
	private String nextoffset;
	private String remainingRecords;
	private ArrayList<Company> companies;
    private String status;
	
	
	
	public CompanyList() {
		super();
	}


	public CompanyList(String nextoffset,String remainingRecords, ArrayList<Company> companies) {
		super();
		this.nextoffset = nextoffset;
		this.remainingRecords = remainingRecords;
		this.companies = companies;
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


	public ArrayList<Company> getCompanies() {
		return companies;
	}


	public void setCompanies(ArrayList<Company> companies) {
		this.companies = companies;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
