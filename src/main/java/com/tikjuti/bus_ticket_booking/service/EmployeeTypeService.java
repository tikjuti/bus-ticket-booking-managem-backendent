package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.Utils.QueryableExtensions;
import com.tikjuti.bus_ticket_booking.dto.request.EmployeeType.EmployeeTypeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.EmployeeType.EmployeeTypeQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.EmployeeType.EmployeeTypeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.EmployeeTypeResponse;
import com.tikjuti.bus_ticket_booking.entity.Employee;
import com.tikjuti.bus_ticket_booking.entity.EmployeeType;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.EmployeeTypeMapper;
import com.tikjuti.bus_ticket_booking.repository.EmployeeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public PaginatedResult<EmployeeType> getEmployeeTypes(EmployeeTypeQueryRequest queryRequest) {
        Map<String, Object> filterParams = new HashMap<>();

        if(queryRequest.getId() != null)
            filterParams.put("id", queryRequest.getId());

        if (queryRequest.getNameEmployeeType() != null)
            filterParams.put("nameEmployeeType", queryRequest.getNameEmployeeType());

        Specification<EmployeeType> spec = Specification.where(
                QueryableExtensions.<EmployeeType>applyIncludes(queryRequest.getIncludes()))
                .and(QueryableExtensions.applyFilters(filterParams))
                .and(QueryableExtensions.applySorting(queryRequest.getSort()));

        return QueryableExtensions.applyPagination(
                employeeTypeRepository, spec, queryRequest.getPage(), queryRequest.getPageSize());
    }

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
