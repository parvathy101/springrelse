package com.iThingsFoundation.IThingsFramework.UploadFile;

import org.springframework.web.multipart.MultipartFile;

import com.iThingsFoundation.IThingsFramework.common.Message;

public class UploadFile {
	
	private MultipartFile file;
	private String filePath;
	private String uuid;
	private Message message;
	private String roleId;
	private String status;
	
	public UploadFile() {
		super();
	}

	public UploadFile(MultipartFile file, String filePath, String uuid, Message message, String roleId, String status) {
		super();
		this.file = file;
		this.filePath = filePath;
		this.uuid = uuid;
		this.message = message;
		this.roleId = roleId;
		this.status = status;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
    


	
	
}

