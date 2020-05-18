package com.iThingsFoundation.IThingsFramework.Physician;

import java.util.ArrayList;



public class PhysicianList {
	
	private String nextoffset;
	private String remainingRecords;
	private ArrayList<Physician> physicians;
    private String status;
    
    
	public PhysicianList() {
		super();
	}


	public PhysicianList(String nextoffset, String remainingRecords, ArrayList<Physician> physicians, String status) {
		super();
		this.nextoffset = nextoffset;
		this.remainingRecords = remainingRecords;
		this.physicians = physicians;
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


	public ArrayList<Physician> getPhysicians() {
		return physicians;
	}


	public void setPhysicians(ArrayList<Physician> physicians) {
		this.physicians = physicians;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

    
    
}
