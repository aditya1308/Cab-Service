package com.service.cab.service.impl;

import java.util.List;
import java.util.Optional;

import com.service.cab.constants.StatusConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.cab.dto.UserDto;
import com.service.cab.entity.Trips;
import com.service.cab.entity.UserEntity;
import com.service.cab.exception.ResourceNotFoundException;
import com.service.cab.repository.UserRepository;
import com.service.cab.service.UserService;
import com.service.cab.util.OTPSenderAndVerificator;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final OTPSenderAndVerificator otpSenderAndVerificator = new OTPSenderAndVerificator();

	


	@Override
	public boolean register(UserDto userDto) {
		if(StatusConstants.isEMAIL_OTP_STATUS() && StatusConstants.isMOBILE_OTP_STATUS()) {
			UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
			userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
			userRepository.save(userEntity);
			return true;
		}
		return false;
	}

	@Override
	public boolean login(UserDto userDto) {
		UserEntity savedUser = userRepository.findByEmail(userDto.getEmail());
		if(savedUser == null)
			throw new ResourceNotFoundException("User does not exist with this email id!");
        return passwordEncoder.matches(userDto.getPassword(), savedUser.getPassword());
    }

	@Override
	public boolean forgotPassword(String email, String password) {
		UserEntity savedUser = userRepository.findByEmail(email);
		if(savedUser!=null) {
			otpSenderAndVerificator.sendOtpToEmail(email);
		}
		if(otpSenderAndVerificator.verifyOtpEmail(email, OTPSenderAndVerificator.getEmailVsOtp().get(email))) {
			savedUser.setPassword(passwordEncoder.encode(password));
			userRepository.save(savedUser);
			return true;
		}	
		return false;
	}

	@Override
	public List<Trips> getAllTripsDetails(Long id) {
		userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		Optional<UserEntity> savedUserEntity = userRepository.findById(id);
		if(savedUserEntity.isPresent())
			return savedUserEntity.get().getTrips();
		
		throw new ResourceNotFoundException("No details found!");
	}

}
