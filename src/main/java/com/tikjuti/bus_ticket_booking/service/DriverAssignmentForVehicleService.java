package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForVehicle.DriverAssignmentForVehicleCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForVehicle.DriverAssignmentForVehicleUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.DriverAssignmentForVehicleResponse;
import com.tikjuti.bus_ticket_booking.entity.DriverAssignmentForVehicle;
import com.tikjuti.bus_ticket_booking.entity.Employee;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.DriverAssignmentForVehicleMapper;
import com.tikjuti.bus_ticket_booking.repository.DriverAssignmentForVehicleRepository;
import com.tikjuti.bus_ticket_booking.repository.EmployeeRepository;
import com.tikjuti.bus_ticket_booking.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DriverAssignmentForVehicleService {
    @Autowired
    private DriverAssignmentForVehicleRepository driverAssignmentForVehicleRepository;

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DriverAssignmentForVehicleMapper driverAssignmentForVehicleMapper;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DriverAssignmentForVehicle createDriverAssignmentForVehicle(
            DriverAssignmentForVehicleCreationRequest request)
    {
        LocalDate startDate = LocalDate.parse(request.getStartDate().trim(), dateFormatter);
        LocalDate endDate = LocalDate.parse(request.getEndDate().trim(), dateFormatter);

        if (!startDate.isBefore(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE);
        }

        Vehicle vehicle = vehicleRepository
                .findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        boolean checkIsDriver = driverAssignmentForVehicleRepository.checkIsDriver(employee.getId());

        if (checkIsDriver) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_A_DRIVER);
        }

//      Một tài xế cos theer phụ trách nhiều xe trong cùng một thời gian

        DriverAssignmentForVehicle driverAssignmentForVehicle = new DriverAssignmentForVehicle();

        driverAssignmentForVehicle.setStartDate(startDate);
        driverAssignmentForVehicle.setEndDate(endDate);
        driverAssignmentForVehicle.setVehicle(vehicle);
        driverAssignmentForVehicle.setEmployee(employee);

        return driverAssignmentForVehicleRepository.save(driverAssignmentForVehicle);
    }

    public List<DriverAssignmentForVehicle> getDriverAssignmentForVehicles()
    {
        return  driverAssignmentForVehicleRepository.findAll();
    }

    public DriverAssignmentForVehicleResponse getDriverAssignmentForVehicle(String driverAssignmentForVehicleId)
    {
        return driverAssignmentForVehicleMapper
                .toDriverAssignmentForVehicleResponse(driverAssignmentForVehicleRepository.findById(driverAssignmentForVehicleId)
                .orElseThrow(() -> new RuntimeException("Driver assignment for vehicle not found")));
    }

    public DriverAssignmentForVehicleResponse updateDriverAssignmentForVehicle(
            DriverAssignmentForVehicleUpdateRequest request, String id)
    {
        DriverAssignmentForVehicle driverAssignmentForVehicle = driverAssignmentForVehicleRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Driver assignment for vehicle not found"));

        LocalDate startDate = LocalDate.parse(request.getStartDate().trim(), dateFormatter);
        LocalDate endDate = LocalDate.parse(request.getEndDate().trim(), dateFormatter);

        if (!startDate.isBefore(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE);
        }

        Vehicle vehicle = vehicleRepository
                .findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        boolean checkIsDriver = driverAssignmentForVehicleRepository.checkIsDriver(employee.getId());

        if (checkIsDriver) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_A_DRIVER);
        }

        driverAssignmentForVehicle.setStartDate(startDate);
        driverAssignmentForVehicle.setEndDate(endDate);
        driverAssignmentForVehicle.setVehicle(vehicle);
        driverAssignmentForVehicle.setEmployee(employee);

        return driverAssignmentForVehicleMapper
                .toDriverAssignmentForVehicleResponse(
                        driverAssignmentForVehicleRepository.save(driverAssignmentForVehicle));
    }

    public DriverAssignmentForVehicleResponse patchUpdateDriverAssignmentForVehicle(
            DriverAssignmentForVehicleUpdateRequest request, String id) {

        DriverAssignmentForVehicle driverAssignmentForVehicle = driverAssignmentForVehicleRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Driver assignment for vehicle not found"));

        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository
                    .findById(request.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            driverAssignmentForVehicle.setVehicle(vehicle);
        }

        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository
                    .findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            boolean checkIsDriver = driverAssignmentForVehicleRepository.checkIsDriver(employee.getId());

            if (checkIsDriver) {
                throw new AppException(ErrorCode.EMPLOYEE_NOT_A_DRIVER);
            }
            driverAssignmentForVehicle.setEmployee(employee);
        }

        if ((request.getStartDate() != null && request.getEndDate() == null) ||
                (request.getStartDate() == null && request.getEndDate() != null)) {

            if (request.getStartDate() != null)
            {
                LocalDate startDate = LocalDate.parse(request.getStartDate().trim(), dateFormatter);
                LocalDate endDate = driverAssignmentForVehicle.getEndDate();

                if (!startDate.isBefore(endDate)) {
                    throw new AppException(ErrorCode.INVALID_DATE);
                }
                driverAssignmentForVehicle.setStartDate(startDate);
            }

            if (request.getStartDate() == null && request.getEndDate() != null)
            {
                LocalDate startDate = driverAssignmentForVehicle.getStartDate();
                LocalDate endDate = LocalDate.parse(request.getEndDate().trim(), dateFormatter);

                if (!startDate.isBefore(endDate)) {
                    throw new AppException(ErrorCode.INVALID_DATE);
                }
                driverAssignmentForVehicle.setEndDate(endDate);
            }
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            LocalDate startDate = LocalDate.parse(request.getStartDate().trim(), dateFormatter);
            LocalDate endDate = LocalDate.parse(request.getEndDate().trim(), dateFormatter);

            if (!startDate.isBefore(endDate)) {
                throw new AppException(ErrorCode.INVALID_DATE);
            }

            driverAssignmentForVehicle.setStartDate(startDate);
            driverAssignmentForVehicle.setEndDate(endDate);
        }

        return driverAssignmentForVehicleMapper.toDriverAssignmentForVehicleResponse(
                driverAssignmentForVehicleRepository.save(driverAssignmentForVehicle));
    }

    public void deleteDriverAssignmentForVehicle(String id) {
        driverAssignmentForVehicleRepository
                .findById(id)
                .map(driverAssignmentForVehicle -> {
                    driverAssignmentForVehicleRepository.delete(driverAssignmentForVehicle);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Driver assignment for vehicle not found for ID: " + id));
    }
}
