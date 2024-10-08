
package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.Customer.CustomerCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Customer.CustomerUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForVehicle.DriverAssignmentForVehicleCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.CustomerResponse;
import com.tikjuti.bus_ticket_booking.dto.response.DriverAssignmentForVehicleResponse;
import com.tikjuti.bus_ticket_booking.entity.Customer;
import com.tikjuti.bus_ticket_booking.entity.DriverAssignmentForVehicle;
import com.tikjuti.bus_ticket_booking.service.CustomerService;
import com.tikjuti.bus_ticket_booking.service.DriverAssignmentForVehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driverAssignmentsForVehicles")
public class DriverAssignmentForVehicleController {
    @Autowired
    private DriverAssignmentForVehicleService driverAssignmentForVehicleService;

    @PostMapping
    ResponseEntity<ApiResponse<DriverAssignmentForVehicle>> createDriverAssignmentForVehicle(
            @RequestBody DriverAssignmentForVehicleCreationRequest request) {

        ApiResponse<DriverAssignmentForVehicle> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Driver assignment for vehicle created successfully");
        apiResponse.setResult(driverAssignmentForVehicleService.createDriverAssignmentForVehicle(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<DriverAssignmentForVehicle>>> getDriverAssignmentForVehicles()
    {
        List<DriverAssignmentForVehicle> driverAssignmentForVehicleList =
                driverAssignmentForVehicleService.getDriverAssignmentForVehicles();

        ApiResponse<List<DriverAssignmentForVehicle>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Driver assignments for vehicles retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(driverAssignmentForVehicleList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<DriverAssignmentForVehicleResponse>> getDriverAssignmentForVehicle(@PathVariable String id) {

        ApiResponse<DriverAssignmentForVehicleResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Driver assignment for vehicle retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(driverAssignmentForVehicleService.getDriverAssignmentForVehicle(id));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

//    @PutMapping("/{customerId}")
//    ResponseEntity<ApiResponse<CustomerResponse>> updateRoute(@PathVariable String customerId, @RequestBody CustomerUpdateRequest request)
//    {
//        ApiResponse<CustomerResponse> apiResponse = new ApiResponse<>();
//
//        apiResponse.setMessage("Customer update successfully");
//        apiResponse.setCode(HttpStatus.OK.value());
//        apiResponse.setResult(customerService.updateCustomer(request, customerId));
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
//
//    @PatchMapping("/{customerId}")
//    ResponseEntity<ApiResponse<CustomerResponse>> updatePatchCustomer(@PathVariable String customerId, @RequestBody CustomerUpdateRequest request)
//    {
//        ApiResponse<CustomerResponse> apiResponse = new ApiResponse<>();
//
//        apiResponse.setMessage("Customer update successfully");
//        apiResponse.setCode(HttpStatus.OK.value());
//        apiResponse.setResult(customerService.patchUpdateCustomer(request, customerId));
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{customerId}")
//    ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
//
//        customerService.deleteCustomer(customerId);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
