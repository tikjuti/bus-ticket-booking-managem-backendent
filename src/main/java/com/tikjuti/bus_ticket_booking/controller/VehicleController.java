package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Vehicle.VehicleCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Vehicle.VehicleUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.RouteResponse;
import com.tikjuti.bus_ticket_booking.dto.response.VehicleResponse;
import com.tikjuti.bus_ticket_booking.entity.Route;
import com.tikjuti.bus_ticket_booking.entity.Seat;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import com.tikjuti.bus_ticket_booking.service.RouteService;
import com.tikjuti.bus_ticket_booking.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    ResponseEntity<ApiResponse<Vehicle>> createVehicle(@RequestBody @Valid VehicleCreationRequest request) {
        ApiResponse<Vehicle> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Vehicle created successfully");
        apiResponse.setResult(vehicleService.createVehicle(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<Vehicle>>> getVehicles()
    {
        List<Vehicle> vehicleList = vehicleService.getVehicles();

        ApiResponse<List<Vehicle>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Vehicles retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(vehicleList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{vehicleId}")
    ResponseEntity<ApiResponse<VehicleResponse>> getVehicle(@PathVariable String vehicleId) {

        ApiResponse<VehicleResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Vehicle retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(vehicleService.getVehicle(vehicleId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
//
    @PutMapping("/{vehicleId}")
    ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(@PathVariable String vehicleId, @RequestBody VehicleUpdateRequest request)
    {
        ApiResponse<VehicleResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Vehicle update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(vehicleService.updateVehicle(request, vehicleId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
//
//    @PatchMapping("/{routeId}")
//    ResponseEntity<ApiResponse<RouteResponse>> updatePatchRoute(@PathVariable String routeId, @RequestBody RouteUpdateRequest request)
//    {
//        ApiResponse<RouteResponse> apiResponse = new ApiResponse<>();
//
//        apiResponse.setMessage("Route update successfully");
//        apiResponse.setCode(HttpStatus.OK.value());
//        apiResponse.setResult(routeService.patchUpdateRoute(request, routeId));
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{routeId}")
//    ResponseEntity<Void> deleteRoute(@PathVariable String routeId) {
//
//        routeService.deleteRoute(routeId);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
