
package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.dto.request.Employee.EmployeeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Employee.EmployeeQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Employee.EmployeeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.EmployeeResponse;
import com.tikjuti.bus_ticket_booking.entity.Employee;
import com.tikjuti.bus_ticket_booking.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    ResponseEntity<ApiResponse<Employee>> createEmployee(@RequestBody EmployeeCreationRequest request) {
        ApiResponse<Employee> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Employee created successfully");
        apiResponse.setResult(employeeService.createEmployee(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<ApiResponse<PaginatedResult<Employee>>> getEmployees(@Valid EmployeeQueryRequest employeeQueryRequest)
    {
        PaginatedResult<Employee> employeeList = employeeService.getEmployees(employeeQueryRequest);

        ApiResponse<PaginatedResult<Employee>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Employees retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(employeeList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    ResponseEntity<ApiResponse<EmployeeResponse>> getEmployee(@PathVariable String employeeId) {

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Employee retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(employeeService.getEmployee(employeeId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{employeeId}")
    ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(@PathVariable String employeeId,
                                                                 @RequestBody EmployeeUpdateRequest request)
    {
        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Employee update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(employeeService.updateEmployee(request, employeeId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{employeeId}")
    ResponseEntity<ApiResponse<EmployeeResponse>> updatePatchEmployee(@PathVariable String employeeId,
                                                                      @RequestBody EmployeeUpdateRequest request)
    {
        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Employee update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(employeeService.patchUpdateEmployee(request, employeeId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}")
    ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId) {

        employeeService.deleteEmployee(employeeId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
