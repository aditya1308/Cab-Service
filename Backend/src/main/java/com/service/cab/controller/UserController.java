package com.service.cab.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.cab.constants.Constants;
import com.service.cab.dto.EmailIdentifier;
import com.service.cab.dto.MobileNoIdentifier;
import com.service.cab.dto.UserDto;
import com.service.cab.entity.Trips;
import com.service.cab.service.UserService;
import com.service.cab.util.OTPSenderAndVerificator;

@RestController
@CrossOrigin("*")
public class UserController {
 
	@Autowired
	private UserService userService;
	
	private OTPSenderAndVerificator otpSenderAndVerificator = new OTPSenderAndVerificator();
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserDto userDto){
		if(userService.register(userDto))
			return new ResponseEntity<String>(Constants.USER_REGISTRATION_SUCCESSFUL,HttpStatus.CREATED);

		return new ResponseEntity<String>(Constants.USER_REGISTRATION_UNSUCCESSFUL,HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/send-email-otp")
	public ResponseEntity<String> sendEmailOtp(@RequestParam String email){
		return new ResponseEntity<String>(otpSenderAndVerificator.sendOtpToEmail(email), HttpStatus.OK);
	}
	
	@PostMapping("/verify-email-otp")
	public ResponseEntity<String> verifyEmailOtp(@RequestBody EmailIdentifier emailIdentifier) {
		if(otpSenderAndVerificator.verifyOtpEmail(emailIdentifier.getEmail(), emailIdentifier.getOtp()))
			return new ResponseEntity<String>(Constants.OTP_VERIFIED_SUCCESSFULLY, HttpStatus.OK);
		
		return new ResponseEntity<String>(Constants.OTP_NOT_VERIFIED, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/send-mobile-otp")
	public ResponseEntity<String> sendMobileOtp(@RequestParam String mobileNo){
		return new ResponseEntity<String>(otpSenderAndVerificator.sendOtpToMobile(mobileNo), HttpStatus.OK);
	}
	
	@PostMapping("/verify-mobile-otp")
	public ResponseEntity<String> verifyMobileOtp(@RequestBody MobileNoIdentifier mobileNoIdentifier) {
		if(otpSenderAndVerificator.verifyOtpMobile(mobileNoIdentifier.getMobileNo(), mobileNoIdentifier.getOtp()))
			return new ResponseEntity<String>(Constants.OTP_VERIFIED_SUCCESSFULLY, HttpStatus.OK);
		
		return new ResponseEntity<String>(Constants.OTP_NOT_VERIFIED, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String>login(@RequestBody UserDto userDto){
		if(userService.login(userDto))
			return new ResponseEntity<String>(Constants.LOGIN_SUCCESSFUL, HttpStatus.OK);
		
		return new ResponseEntity<String>(Constants.LOGIN_UNSUCCESSFUL, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<String>forgotPassword(@RequestParam String email, @RequestParam String password){
		if(userService.forgotPassword(email,password)) {
			return new ResponseEntity<String>(Constants.PASSWORD_CHANGE_SUCCESSFUL, HttpStatus.OK);
		}
		return new ResponseEntity<String>(Constants.PASSWORD_CHANGE_NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/{userId}/trips-list")
	public ResponseEntity<List<Trips>>listOfTrips(@PathVariable Long userId){
		return new ResponseEntity<List<Trips>>(userService.getAllTripsDetails(userId),HttpStatus.OK);
	}
}
