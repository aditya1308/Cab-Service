package com.service.cab.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.service.cab.dto.TripsDto;
import com.service.cab.service.TripsService;

@RestController
@CrossOrigin("*")
public class TripsController {

	private static final Logger log = LoggerFactory.getLogger(TripsController.class);
	@Autowired
	private TripsService tripsService;



	@PostMapping("/{userId}/book-trip")
	public ResponseEntity<?> calculateDistance(@PathVariable Long userId, @RequestBody TripsDto tripsRequest) {
    	TripsDto createdTrip = tripsService.createTrip(userId, tripsRequest);
    	if(createdTrip != null)
    		return new ResponseEntity<TripsDto>(createdTrip,HttpStatus.OK);
		
    	log.warn("Trips request {}:", tripsRequest);
		return new ResponseEntity<String>("Booking could not be done, Something went wrong!",HttpStatus.INTERNAL_SERVER_ERROR);
    }

	@GetMapping("/{userId}/trips")
	public ResponseEntity<?> getUserTrips(@PathVariable Long userId, @RequestBody TripsDto tripsRequest) {
		TripsDto createdTrip = tripsService.getUserTripDetails(userId, tripsRequest);
		if(createdTrip != null){
			return new ResponseEntity<TripsDto>(createdTrip,HttpStatus.OK);
		}
		return new ResponseEntity<String>("Trips details cannot be fetched!",HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
