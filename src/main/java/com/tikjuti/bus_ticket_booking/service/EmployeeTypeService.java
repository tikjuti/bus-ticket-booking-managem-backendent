package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.EmployeeType.EmployeeTypeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.EmployeeType.EmployeeTypeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.EmployeeTypeResponse;
import com.tikjuti.bus_ticket_booking.entity.EmployeeType;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.EmployeeTypeMapper;
import com.tikjuti.bus_ticket_booking.repository.EmployeeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeTypeService {
    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private EmployeeTypeMapper employeeTypeMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeType createEmployeeType(EmployeeTypeCreationRequest request)
    {
        if(employeeTypeRepository.existsByNameEmployeeType(request.getNameEmployeeType()))
            throw new AppException(ErrorCode.EMPLOYEE_TYPE_EXISTED);

        EmployeeType employeeType = employeeTypeMapper.toEmployeeType(request);

        return employeeTypeRepository
                .save(employeeType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<EmployeeType> getEmployeeTypes() {return  employeeTypeRepository.findAll();}

    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeTypeResponse getEmployeeType(String employeeTypeId)
    {
        return employeeTypeMapper
                .toEmployeeTypeResponse(employeeTypeRepository.findById(employeeTypeId)
                .orElseThrow(() -> new RuntimeException("Employee type not found")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeTypeResponse updateEmployeeType(EmployeeTypeUpdateRequest request, String employeeTypeId)
    {
        EmployeeType employeeType = employeeTypeRepository
                .findById(employeeTypeId)
                .orElseThrow(() -> new RuntimeException("Employee type not found"));

        if(employeeTypeRepository.existsByNameEmployeeType(request.getNameEmployeeType()))
            throw new AppException(ErrorCode.EMPLOYEE_TYPE_EXISTED);

        employeeTypeMapper.updateEmployeeType(employeeType, request);

        return employeeTypeMapper
                .toEmployeeTypeResponse(employeeTypeRepository.save(employeeType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmployeeType(String employeeTypeId) {
        employeeTypeRepository.findById(employeeTypeId)
                .map(employeeType -> {
                    employeeTypeRepository.delete(employeeType);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Employee type not found for ID: " + employeeTypeId));
    }
}
