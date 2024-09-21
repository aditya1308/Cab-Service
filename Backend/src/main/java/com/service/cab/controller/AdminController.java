package com.service.cab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.cab.constants.Constants;
import com.service.cab.dto.AdminDto;
import com.service.cab.service.AdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

	@Autowired
	private AdminService adminService;
		
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody AdminDto adminDto){
		if(adminService.register(adminDto))
			return new ResponseEntity<String>(Constants.USER_REGISTRATION_SUCCESSFUL,HttpStatus.CREATED);

		return new ResponseEntity<String>(Constants.USER_REGISTRATION_UNSUCCESSFUL,HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String>login(@RequestBody AdminDto adminDto){
		if(adminService.login(adminDto))
			return new ResponseEntity<String>(Constants.LOGIN_SUCCESSFUL, HttpStatus.OK);
		
		return new ResponseEntity<String>(Constants.LOGIN_UNSUCCESSFUL, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<String>forgotPassword(@RequestParam String email, @RequestParam String password){
		if(adminService.forgotPassword(email,password)) {
			return new ResponseEntity<String>(Constants.PASSWORD_CHANGE_SUCCESSFUL, HttpStatus.OK);
		}
		return new ResponseEntity<String>(Constants.PASSWORD_CHANGE_NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
	}
}
