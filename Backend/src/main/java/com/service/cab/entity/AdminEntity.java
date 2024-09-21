package com.service.cab.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "cab_admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID uuid;
	private String firstName;
	private String lastName;
	@NotNull
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number is not valid")
	private String mobileNo;
	@NotNull
	@Email(message = "Email is not valid")
	private String email;
	private String gender;
	private String password;
	
	private String adminCode;
}
