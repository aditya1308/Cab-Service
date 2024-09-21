package com.service.cab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.cab.dto.UserDto;
import com.service.cab.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{
	UserEntity findByEmail(String email);
	Optional<UserEntity> findById(Long id);
}
