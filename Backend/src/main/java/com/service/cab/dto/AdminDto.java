package com.service.cab.dto;

import java.util.UUID;

public class AdminDto extends UserDto{
	
	private Long uuid;
	private String adminCode;

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	public String getAdminCode() {
		return adminCode;
	}

	public void setAdminCode(String adminCode) {
		this.adminCode = adminCode;
	}
	

}
