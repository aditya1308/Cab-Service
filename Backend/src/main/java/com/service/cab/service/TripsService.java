package com.service.cab.service;

import java.util.List;

import com.service.cab.dto.TripsDto;

public interface TripsService {
	TripsDto createTrip(Long userId, TripsDto trips);
	TripsDto getUserTripDetails(Long userId, TripsDto trips);
}
