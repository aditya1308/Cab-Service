package com.service.cab.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.cab.constants.Constants;
import com.service.cab.constants.StatusConstants;
import com.service.cab.dto.AdminDto;
import com.service.cab.entity.AdminEntity;
import com.service.cab.exception.InvalidDataException;
import com.service.cab.repository.AdminRepository;
import com.service.cab.service.AdminService;
import com.service.cab.util.OTPSenderAndVerificator;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AdminRepository adminRepository;
	
	private OTPSenderAndVerificator otpSenderAndVerificator = new OTPSenderAndVerificator();


	@Override
	public boolean register(AdminDto adminDto) {
		if(StatusConstants.isEMAIL_OTP_STATUS() && StatusConstants.isMOBILE_OTP_STATUS()) {
				if(adminDto.getAdminCode().equals(Constants.ADMIN_CODE)) {
				AdminEntity adminEntity = modelMapper.map(adminDto, AdminEntity.class);
				adminEntity.setPassword(passwordEncoder.encode(adminDto.getPassword()));
				adminRepository.save(adminEntity);
				return true;
			}
				throw new InvalidDataException("Admin Code is not valid");
		}
		return false;
		
	}

	@Override
	public boolean login(AdminDto adminDto) {
		AdminEntity savedAdmin = adminRepository.findByEmail(adminDto.getEmail());
		if(savedAdmin!=null && passwordEncoder.matches(adminDto.getPassword(), savedAdmin.getPassword())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean forgotPassword(String email, String password) {
		AdminEntity savedAdmin = adminRepository.findByEmail(email);
		if(savedAdmin!=null) {
			otpSenderAndVerificator.sendOtpToEmail(email);
		}
		if(otpSenderAndVerificator.verifyOtpEmail(email, OTPSenderAndVerificator.getEmailVsOtp().get(email))) {
			savedAdmin.setPassword(passwordEncoder.encode(password));
			adminRepository.save(savedAdmin);
			return true;
		}	
		return false;
	}
}
