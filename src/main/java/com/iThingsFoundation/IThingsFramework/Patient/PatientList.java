package com.iThingsFoundation.IThingsFramework.Patient;

import java.util.ArrayList;



public class PatientList {
	private String nextoffset;
	private String remainingRecords;
	private ArrayList<Patient> patient;
    private String status;
    
	public PatientList() {
		super();
	}

	public PatientList(String nextoffset, String remainingRecords, ArrayList<Patient> patient, String status) {
		super();
		this.nextoffset = nextoffset;
		this.remainingRecords = remainingRecords;
		this.patient = patient;
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

	public ArrayList<Patient> getPatient() {
		return patient;
	}

	public void setPatient(ArrayList<Patient> patient) {
		this.patient = patient;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
    
}
