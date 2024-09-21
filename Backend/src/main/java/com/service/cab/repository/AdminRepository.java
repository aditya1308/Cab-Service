package com.service.cab.repository;

import java.util.UUID;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.cab.entity.AdminEntity;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity,UUID>{
	AdminEntity findByEmail(String email);

}
