package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.VehicleType.VehicleTypeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.VehicleType.VehicleTypeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.VehicleTypeResponse;
import com.tikjuti.bus_ticket_booking.entity.VehicleType;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.VehicleTypeMapper;
import com.tikjuti.bus_ticket_booking.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleTypeService {
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private VehicleTypeMapper vehicleTypeMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public VehicleType createVehicleType(VehicleTypeCreationRequest request)
    {
        if(vehicleTypeRepository.existsByVehicleTypeName(request.getVehicleTypeName()))
            throw new AppException(ErrorCode.VEHICLE_TYPE_EXISTED);

        VehicleType vehicleType = vehicleTypeMapper.toVehicleType(request);

        return vehicleTypeRepository
                .save(vehicleType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<VehicleType> getVehicleTypes() {return  vehicleTypeRepository.findAll();}

    @PreAuthorize("hasRole('ADMIN')")
    public VehicleTypeResponse getVehicleType(String vehicleTypeId)
    {
        return vehicleTypeMapper
                .toVehicleTypeResponse(vehicleTypeRepository.findById(vehicleTypeId)
                .orElseThrow(() -> new RuntimeException("Vehicle type not found")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public VehicleTypeResponse updateVehicleType(VehicleTypeUpdateRequest request, String vehicleTypeId)
    {
        VehicleType vehicleType = vehicleTypeRepository
                .findById(vehicleTypeId)
                .orElseThrow(() -> new RuntimeException("Vehicle type not found"));

        if(vehicleTypeRepository.existsByVehicleTypeName(request.getVehicleTypeName()))
            throw new AppException(ErrorCode.VEHICLE_TYPE_EXISTED);

        vehicleTypeMapper.updateVehicleType(vehicleType, request);

        return vehicleTypeMapper
                .toVehicleTypeResponse(vehicleTypeRepository.save(vehicleType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteVehicleType(String vehicleTypeId) {
        vehicleTypeRepository.findById(vehicleTypeId)
                .map(vehicleType -> {
                    vehicleTypeRepository.delete(vehicleType);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Vehicle type not found for ID: " + vehicleTypeId));
    }
}
