package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.EmployeeType.EmployeeTypeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.EmployeeType.EmployeeTypeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.EmployeeTypeResponse;
import com.tikjuti.bus_ticket_booking.entity.EmployeeType;
import com.tikjuti.bus_ticket_booking.service.EmployeeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employeeTypes")
public class EmployeeTypeController {
    @Autowired
    private EmployeeTypeService employeeTypeService;

    @PostMapping
    ResponseEntity<ApiResponse<EmployeeType>> createEmployeeType(@RequestBody EmployeeTypeCreationRequest request)
    {
        ApiResponse<EmployeeType> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Employee type created successfully");
        apiResponse.setResult(employeeTypeService.createEmployeeType(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @GetMapping
    ResponseEntity<ApiResponse<List<EmployeeType>>> getEmployeeTypes()
    {
        List<EmployeeType> employeeTypeList = employeeTypeService.getEmployeeTypes();

        ApiResponse<List<EmployeeType>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Employee types retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(employeeTypeList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }


    @GetMapping("/{employeeTypeId}")
    ResponseEntity<ApiResponse<EmployeeTypeResponse>> getEmployeeType(@PathVariable String employeeTypeId) {

        ApiResponse<EmployeeTypeResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Employee type retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(employeeTypeService.getEmployeeType(employeeTypeId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @PutMapping("/{employeeTypeId}")
    ResponseEntity<ApiResponse<EmployeeTypeResponse>> updateEmployeeType(@PathVariable String employeeTypeId,
                                                                         @RequestBody EmployeeTypeUpdateRequest request)
    {
        ApiResponse<EmployeeTypeResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Employee type update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(employeeTypeService.updateEmployeeType(request, employeeTypeId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @DeleteMapping("/{employeeTypeId}")
    ResponseEntity<Void> deleteEmployeeType(@PathVariable String employeeTypeId) {

            employeeTypeService.deleteEmployeeType(employeeTypeId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
