package com.iThingsFoundation.IThingsFramework.Device;

import com.iThingsFoundation.IThingsFramework.common.DeviceFilters;
import com.iThingsFoundation.IThingsFramework.common.Message;

public class Device {
	
	private Object deviceuuid;
	private Object tenantId;
	private Object customerId;
	private Object physicianId;
	private Object patientId;
	private String deviceId;
	private String deviceNumber;
	private String firmwareVersion;
	private String modelNo;
	private Message message;
	private String batchsize;
	private String offset;
	private DeviceFilters filter;
	
	
	
	public Device() {
		super();
	}



	public Device(Object deviceuuid, Object tenantId, Object customerId, Object physicianId, Object patientId,
			String deviceId, String deviceNumber, String firmwareVersion, String modelNo, Message message,
			String batchsize, String offset, DeviceFilters filter) {
		super();
		this.deviceuuid = deviceuuid;
		this.tenantId = tenantId;
		this.customerId = customerId;
		this.physicianId = physicianId;
		this.patientId = patientId;
		this.deviceId = deviceId;
		this.deviceNumber = deviceNumber;
		this.firmwareVersion = firmwareVersion;
		this.modelNo = modelNo;
		this.message = message;
		this.batchsize = batchsize;
		this.offset = offset;
		this.filter = filter;
	}







	public Object getDeviceuuid() {
		return deviceuuid;
	}



	public void setDeviceuuid(Object deviceuuid) {
		this.deviceuuid = deviceuuid;
	}



	public Object getTenantId() {
		return tenantId;
	}



	public void setTenantId(Object tenantId) {
		this.tenantId = tenantId;
	}



	public Object getCustomerId() {
		return customerId;
	}



	public void setCustomerId(Object customerId) {
		this.customerId = customerId;
	}



	public Object getPhysicianId() {
		return physicianId;
	}



	public void setPhysicianId(Object physicianId) {
		this.physicianId = physicianId;
	}



	public Object getPatientId() {
		return patientId;
	}



	public void setPatientId(Object patientId) {
		this.patientId = patientId;
	}



	public String getDeviceId() {
		return deviceId;
	}



	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}



	public String getDeviceNumber() {
		return deviceNumber;
	}



	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
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



	public DeviceFilters getFilter() {
		return filter;
	}



	public void setFilter(DeviceFilters filter) {
		this.filter = filter;
	}
	
	
	

}
