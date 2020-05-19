package com.iThingsFoundation.IThingsFramework.common;

public class DeviceFilters {
	
	private String deviceId;
	private String firmwareVersion;
	private String modelNo;
	private String deviceNumber;
		
	public DeviceFilters() {
		super();
	}




	public DeviceFilters(String deviceId, String firmwareVersion, String modelNo, String deviceNumber) {
		super();
		this.deviceId = deviceId;
		this.firmwareVersion = firmwareVersion;
		this.modelNo = modelNo;
		this.deviceNumber = deviceNumber;
	}


	public String getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public String getFirmwareVersion() {
		return firmwareVersion;
	}


	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}


	public String getModelNo() {
		return modelNo;
	}


	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}




	public String getDeviceNumber() {
		return deviceNumber;
	}




	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	
	

	
	
	

}
