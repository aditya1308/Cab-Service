package com.service.cab.service.impl;

import com.service.cab.constants.PaymentStatus;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.service.cab.dto.TripsDto;
import com.service.cab.entity.Trips;
import com.service.cab.entity.UserEntity;
import com.service.cab.exception.InvalidDataException;
import com.service.cab.exception.ResourceNotFoundException;
import com.service.cab.repository.TripsRepository;
import com.service.cab.repository.UserRepository;
import com.service.cab.service.TripsService;
import com.service.cab.util.DistanceMatrixResponse;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
@Service
public class TripsServiceImpl implements TripsService{
	
	private final OkHttpClient httpClient = new OkHttpClient();
	private  DistanceMatrixResponse distanceMatrixResponse = new DistanceMatrixResponse();
	
	@Autowired
	TripsRepository tripsRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	private final String API_KEY;
	private final String DISTANCE_MATRIX_API_KEY;

	private TripsServiceImpl(@Value("${api-key}") String apiKey, @Value("${distance-matrix-api}")String distanceMatrixApiKey) {
		this.API_KEY = apiKey;
        this.DISTANCE_MATRIX_API_KEY = distanceMatrixApiKey;
    }

	@Override
	public TripsDto createTrip(Long userId, TripsDto tripsRequest) {
		Trips trips = modelMapper.map(tripsRequest, Trips.class);
		UserEntity userEntity = userRepository.findById(userId)
											  .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
		String origin = trips.getOrigin();
        String destination = trips.getDestination();
        String url = String.format(DISTANCE_MATRIX_API_KEY,origin, destination, API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .build();

		if(tripsRequest.getDateOfPickup().isAfter(tripsRequest.getDateOfReturn())){
			throw new InvalidDataException("Date of pickup should be before date of return");
		}
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
            	/**
            	 * return response.body().string(), closes connection automatically,
            	 * that's using response.peekBody(2048).string();
            	 */
            	distanceMatrixResponse.setResponse(response.peekBody(2048).string().toString());
            	JSONObject jsonObject = new JSONObject(response.peekBody(2048).string().toString());
                Integer distanceText = jsonObject.getJSONArray("rows")
                					   .getJSONObject(0)
                					   .getJSONArray("elements")
                					   .getJSONObject(0)
                					   .getJSONObject("distance")
                					   .getInt("value");
                response.close();
                trips.setDistance((double)distanceText/1000);
                trips.setAmount((long) (trips.getDistance()*20)); // fare could be adjusted
                trips.setUserEntity(userEntity);
                Trips createdTrip = tripsRepository.save(trips);
                return modelMapper.map(createdTrip, TripsDto.class);
            } else {
                throw new InvalidDataException("Trip creation error!");
            }
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
	}

	@Override
	public TripsDto getUserTripDetails(Long userId, TripsDto trips) {
		Trips tripsReq = modelMapper.map(trips, Trips.class);
		UserEntity savedUser = userRepository.findById(userId)
				                             .orElseThrow(()-> new ResourceNotFoundException("User not found with id" + userId));
		return modelMapper.map(savedUser.getTrips(),TripsDto.class);
	}
}
