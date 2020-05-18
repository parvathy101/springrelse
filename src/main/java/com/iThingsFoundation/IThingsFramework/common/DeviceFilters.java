package com.iThingsFoundation.IThingsFramework.common;

public class DeviceFilters {
	
	private String deviceId;
	private String firmwareVersion;
	private String modelNo;
		
	public DeviceFilters() {
		super();
	}


	public DeviceFilters(String deviceId, String version, String model) {
		super();
		this.deviceId = deviceId;
		this.firmwareVersion = version;
		this.modelNo = model;
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


	
	
	

}
