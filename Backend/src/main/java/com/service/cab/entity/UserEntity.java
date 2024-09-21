package com.service.cab.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cab_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
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
	
	@OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<Trips> trips;
}
