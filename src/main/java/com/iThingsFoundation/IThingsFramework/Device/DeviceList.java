package com.iThingsFoundation.IThingsFramework.Device;

import java.util.ArrayList;


public class DeviceList {
	
	private String nextoffset;
	private String remainingRecords;
	private ArrayList<Device> device;
    private String status;
    
       
	public DeviceList() {
		super();
	}


	public DeviceList(String nextoffset, String remainingRecords, ArrayList<Device> device, String status) {
		super();
		this.nextoffset = nextoffset;
		this.remainingRecords = remainingRecords;
		this.device = device;
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


	public ArrayList<Device> getDevice() {
		return device;
	}


	public void setDevice(ArrayList<Device> device) {
		this.device = device;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

    
    
    
}
