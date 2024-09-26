package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.EmployeeTypeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.EmployeeTypeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.EmployeeTypeResponse;
import com.tikjuti.bus_ticket_booking.entity.EmployeeType;
import com.tikjuti.bus_ticket_booking.service.EmployeeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employeeTypes")
public class EmployeeTypeController {
    @Autowired
    private EmployeeTypeService employeeTypeService;

    @PostMapping
    ApiResponse<EmployeeType> createEmployeeType(@RequestBody EmployeeTypeCreationRequest request)
    {
        ApiResponse<EmployeeType> apiResponse = new ApiResponse<>();

        apiResponse.setResult(employeeTypeService.createEmployeeType(request));

        return apiResponse;
    }

    @GetMapping
    List<EmployeeType> getEmployeeTypes() {return employeeTypeService.getEmployeeTypes();}

    @GetMapping("/{employeeTypeId}")
    EmployeeTypeResponse getEmployeeType(@PathVariable String employeeTypeId) {
        return employeeTypeService.getEmployeeType(employeeTypeId);
    }

    @PutMapping("/{employeeTypeId}")
    EmployeeTypeResponse updateEmployeeType(@PathVariable String employeeTypeId, @RequestBody EmployeeTypeUpdateRequest request)
    {
        return employeeTypeService.updateEmployeeType(request, employeeTypeId);
    }

    @DeleteMapping("/{employeeTypeId}")
    ApiResponse<EmployeeType> deleteEmployeeType(@PathVariable String employeeTypeId) {
        try {
            boolean isDeleted = employeeTypeService.deleteEmployeeType(employeeTypeId);

            if (isDeleted) {
                return new ApiResponse<EmployeeType>(HttpStatus.OK.value(), "Employee type has been deleted", null);
            } else {
                return new ApiResponse<EmployeeType>(HttpStatus.NOT_FOUND.value(), "Employee type not found", null);
            }
        } catch (Exception e) {
            return new ApiResponse<EmployeeType>( HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while deleting the employee type", null);
        }
    }
}
