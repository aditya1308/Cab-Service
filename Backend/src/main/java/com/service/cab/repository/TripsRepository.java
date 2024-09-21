package com.service.cab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.cab.dto.TripsDto;
import com.service.cab.entity.Trips;

public interface TripsRepository extends JpaRepository<Trips, Long>{
	Optional<Trips> findById(Long id);

}
