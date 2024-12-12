package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.dto.request.Trip.TripCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Trip.TripQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Trip.TripUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.TripResponse;
import com.tikjuti.bus_ticket_booking.entity.Trip;
import com.tikjuti.bus_ticket_booking.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/trips")
public class TripController {
    @Autowired
    private TripService tripService;

    @PostMapping
    ResponseEntity<ApiResponse<Trip>> createTrip(@RequestBody @Valid TripCreationRequest request) {
        ApiResponse<Trip> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Trip created successfully");
        apiResponse.setResult(tripService.createTrip(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<ApiResponse<PaginatedResult<Trip>>> getTrips(TripQueryRequest queryRequest) {
        PaginatedResult<Trip> tripList = tripService.getTrips(queryRequest);

        ApiResponse<PaginatedResult<Trip>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Trips retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(tripList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/unassigned")
    ResponseEntity<ApiResponse<List<Trip>>> getTrips() {
        List<Trip> tripList = tripService.getUnassignedTrips();

        ApiResponse<List<Trip>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Trips retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(tripList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{tripId}")
    ResponseEntity<ApiResponse<TripResponse>> getTrip(@PathVariable String tripId) {

        ApiResponse<TripResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Trip retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(tripService.getTrip(tripId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{tripId}")
    ResponseEntity<ApiResponse<TripResponse>> updateTrip(@PathVariable String tripId,
                                                         @RequestBody TripUpdateRequest request)
    {
        ApiResponse<TripResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Trip update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(tripService.updateTrip(request, tripId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{tripId}")
    ResponseEntity<ApiResponse<TripResponse>> updatePatchTrip(@PathVariable String tripId,
                                                              @RequestBody TripUpdateRequest request)
    {
        ApiResponse<TripResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Trip update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(tripService.patchUpdateTrip(request, tripId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{tripId}")
    ResponseEntity<Void> deleteTrip(@PathVariable String tripId) {

        tripService.deleteTrip(tripId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
