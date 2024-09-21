package com.service.cab.service;

import com.service.cab.dto.AdminDto;
import com.service.cab.dto.UserDto;

public interface AdminService {
	boolean register(AdminDto adminDto);
	boolean login(AdminDto adminDto);
	boolean forgotPassword(String email, String password);

}
