
package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.Customer.CustomerCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Customer.CustomerUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForTrip.DriverAssignmentForTripCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForTrip.DriverAssignmentForTripUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.CustomerResponse;
import com.tikjuti.bus_ticket_booking.dto.response.DriverAssignmentForTripResponse;
import com.tikjuti.bus_ticket_booking.entity.Customer;
import com.tikjuti.bus_ticket_booking.entity.DriverAssignmentForTrip;
import com.tikjuti.bus_ticket_booking.service.CustomerService;
import com.tikjuti.bus_ticket_booking.service.DriverAssignmentForTripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driverAssignmentsForTrips")
public class DriverAssignmentForTripController {
    @Autowired
    private DriverAssignmentForTripService driverAssignmentForTripService;

    @PostMapping
    ResponseEntity<ApiResponse<DriverAssignmentForTrip>> createDriverAssignmentForTrip(
            @RequestBody DriverAssignmentForTripCreationRequest request) {

        ApiResponse<DriverAssignmentForTrip> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Driver assignment for trip created successfully");
        apiResponse.setResult(driverAssignmentForTripService.createDriverAssignmentForTrip(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<DriverAssignmentForTrip>>> getDriverAssignmentsForTrips()
    {
        List<DriverAssignmentForTrip> driverAssignmentForTripList =
                driverAssignmentForTripService.getDriverAssignmentForTrips();

        ApiResponse<List<DriverAssignmentForTrip>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Driver assignment for trip retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(driverAssignmentForTripList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<DriverAssignmentForTripResponse>> getDriverAssignmentForTrip(
            @PathVariable String id) {

        ApiResponse<DriverAssignmentForTripResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Driver assignment for trip retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(driverAssignmentForTripService.getDriverAssignmentForTrip(id));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<DriverAssignmentForTripResponse>> updateDriverAssignmentForTrip(
            @PathVariable String id, @RequestBody DriverAssignmentForTripUpdateRequest request)
    {
        ApiResponse<DriverAssignmentForTripResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Driver assignment for trip update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(driverAssignmentForTripService.
                updateDriverAssignmentForTrip(request, id));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    ResponseEntity<ApiResponse<DriverAssignmentForTripResponse>> updatePatchDriverAssignmentForTrip(
            @PathVariable String id, @RequestBody DriverAssignmentForTripUpdateRequest request)
    {
        ApiResponse<DriverAssignmentForTripResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Driver assignment for trip update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(driverAssignmentForTripService.patchUpdateDriverAssignmentForTrip(request, id));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteDriverAssignmentForTrip(@PathVariable String id) {

        driverAssignmentForTripService.deleteDriverAssignmentForTrip(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
