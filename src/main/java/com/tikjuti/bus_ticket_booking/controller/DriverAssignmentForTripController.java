
package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.Customer.CustomerCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Customer.CustomerUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.CustomerResponse;
import com.tikjuti.bus_ticket_booking.entity.Customer;
import com.tikjuti.bus_ticket_booking.service.CustomerService;
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
    private CustomerService customerService;

    @PostMapping
    ResponseEntity<ApiResponse<Customer>> createCustomer(@RequestBody @Valid CustomerCreationRequest request) {
        ApiResponse<Customer> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Customer created successfully");
        apiResponse.setResult(customerService.createCustomer(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<Customer>>> getCustomers()
    {
        List<Customer> customersList = customerService.getCustomers();

        ApiResponse<List<Customer>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Customers retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(customersList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(@PathVariable String customerId) {

        ApiResponse<CustomerResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Customer retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(customerService.getCustomer(customerId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{customerId}")
    ResponseEntity<ApiResponse<CustomerResponse>> updateRoute(@PathVariable String customerId, @RequestBody CustomerUpdateRequest request)
    {
        ApiResponse<CustomerResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Customer update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(customerService.updateCustomer(request, customerId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{customerId}")
    ResponseEntity<ApiResponse<CustomerResponse>> updatePatchCustomer(@PathVariable String customerId, @RequestBody CustomerUpdateRequest request)
    {
        ApiResponse<CustomerResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Customer update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(customerService.patchUpdateCustomer(request, customerId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}")
    ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {

        customerService.deleteCustomer(customerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
