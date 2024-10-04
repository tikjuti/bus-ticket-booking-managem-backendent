package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Trip.TripCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.entity.Route;
import com.tikjuti.bus_ticket_booking.entity.Trip;
import com.tikjuti.bus_ticket_booking.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

//    @GetMapping
//    ResponseEntity<ApiResponse<List<Route>>> getRoutes()
//    {
//        List<Route> routeList = routeService.getRoutes();
//
//        ApiResponse<List<Route>> apiResponse = new ApiResponse<>();
//
//        apiResponse.setMessage("Routes retrieved successfully");
//        apiResponse.setCode(HttpStatus.OK.value());
//        apiResponse.setResult(routeList);
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
//
//    @GetMapping("/{routeId}")
//    ResponseEntity<ApiResponse<RouteResponse>> getRoute(@PathVariable String routeId) {
//
//        ApiResponse<RouteResponse> apiResponse = new ApiResponse<>();
//
//        apiResponse.setMessage("Route retrieved successfully");
//        apiResponse.setCode(HttpStatus.OK.value());
//        apiResponse.setResult(routeService.getRoute(routeId));
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
//
//    @PutMapping("/{routeId}")
//    ResponseEntity<ApiResponse<RouteResponse>> updateRoute(@PathVariable String routeId, @RequestBody RouteUpdateRequest request)
//    {
//        ApiResponse<RouteResponse> apiResponse = new ApiResponse<>();
//
//        apiResponse.setMessage("Route update successfully");
//        apiResponse.setCode(HttpStatus.OK.value());
//        apiResponse.setResult(routeService.updateRoute(request, routeId));
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
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
