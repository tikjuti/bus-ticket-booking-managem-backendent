package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.Utils.QueryableExtensions;
import com.tikjuti.bus_ticket_booking.dto.request.VehicleType.VehicleTypeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.VehicleType.VehicleTypeQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.VehicleType.VehicleTypeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.VehicleTypeResponse;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import com.tikjuti.bus_ticket_booking.entity.VehicleType;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.VehicleTypeMapper;
import com.tikjuti.bus_ticket_booking.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public PaginatedResult<VehicleType> getVehicleTypes(VehicleTypeQueryRequest queryRequest) {
        Map<String, Object> filterParams = new HashMap<>();

        if (queryRequest.getId() != null)
            filterParams.put("id", queryRequest.getId());

        if (queryRequest.getVehicleTypeName() != null)
            filterParams.put("vehicleTypeName", queryRequest.getVehicleTypeName());

        Specification<VehicleType> spec = Specification.where(
                        QueryableExtensions.<VehicleType>applyIncludes(queryRequest.getIncludes()))
                .and(QueryableExtensions.applyFilters(filterParams))
                .and(QueryableExtensions.applySorting(queryRequest.getSort()));

        return QueryableExtensions.applyPagination(
                vehicleTypeRepository, spec, queryRequest.getPage(), queryRequest.getPageSize());
    }

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
