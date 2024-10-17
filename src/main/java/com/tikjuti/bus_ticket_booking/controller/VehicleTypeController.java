package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.VehicleType.VehicleTypeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.VehicleType.VehicleTypeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.VehicleTypeResponse;
import com.tikjuti.bus_ticket_booking.entity.VehicleType;
import com.tikjuti.bus_ticket_booking.service.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicleTypes")
public class VehicleTypeController {
    @Autowired
    private VehicleTypeService vehicleTypeService;

    @PostMapping
    ResponseEntity<ApiResponse<VehicleType>> createVehicleType(@RequestBody VehicleTypeCreationRequest request)
    {
        ApiResponse<VehicleType> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Vehicle type created successfully");
        apiResponse.setResult(vehicleTypeService.createVehicleType(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @GetMapping
    ResponseEntity<ApiResponse<List<VehicleType>>> getVehicleTypes()
    {
        List<VehicleType> vehicleTypeList = vehicleTypeService.getVehicleTypes();

        ApiResponse<List<VehicleType>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Vehicle types retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(vehicleTypeList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }


    @GetMapping("/{vehicleTypeId}")
    ResponseEntity<ApiResponse<VehicleTypeResponse>> getVehicleType(@PathVariable String vehicleTypeId) {

        ApiResponse<VehicleTypeResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Vehicle type retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(vehicleTypeService.getVehicleType(vehicleTypeId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @PutMapping("/{vehicleTypeId}")
    ResponseEntity<ApiResponse<VehicleTypeResponse>> updateVehicleType(@PathVariable String vehicleTypeId,
                                                                       @RequestBody VehicleTypeUpdateRequest request)
    {
        ApiResponse<VehicleTypeResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Vehicle type update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(vehicleTypeService.updateVehicleType(request, vehicleTypeId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @DeleteMapping("/{vehicleTypeId}")
    ResponseEntity<Void> deleteVehicleType(@PathVariable String vehicleTypeId) {

            vehicleTypeService.deleteVehicleType(vehicleTypeId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
