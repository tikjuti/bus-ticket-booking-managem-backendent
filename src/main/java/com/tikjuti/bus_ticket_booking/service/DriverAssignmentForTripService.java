package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForTrip.DriverAssignmentForTripCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForTrip.DriverAssignmentForTripUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.DriverAssignmentForTripResponse;
import com.tikjuti.bus_ticket_booking.entity.*;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.DriverAssignmentForTripMapper;
import com.tikjuti.bus_ticket_booking.repository.DriverAssignmentForTripRepository;
import com.tikjuti.bus_ticket_booking.repository.EmployeeRepository;
import com.tikjuti.bus_ticket_booking.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverAssignmentForTripService {
    @Autowired
    private DriverAssignmentForTripRepository driverAssignmentForTripRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private DriverAssignmentForTripMapper driverAssignmentForTripMapper;

    public DriverAssignmentForTrip createDriverAssignmentForTrip(
            DriverAssignmentForTripCreationRequest request)
    {
        Trip trip = tripRepository
                .findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        boolean checkIsDriver = driverAssignmentForTripRepository.
                checkIsDriver(employee.getId());

//        Kiểm tra nhân viên có phải là tài xế không
        if (checkIsDriver) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_A_DRIVER);
        }

        boolean canDriverOperateVehicle = driverAssignmentForTripRepository.
                canDriverOperateVehicle(
                        request.getTripId(),
                        request.getEmployeeId()
                );
//        Kiểm tra tài xế có đc phân công phụ trách xe không
        if (canDriverOperateVehicle) {
            throw new AppException(ErrorCode.DRIVER_CAN_NOT_OPERATE_VEHICLE);
        }
//        Kiểm tra xem tài xế đã được phân công cho chuyến đi chưa
        boolean checkDriverAssignmentForTripExists = driverAssignmentForTripRepository.
                checkDriverAssignmentForTripExists(
                        request.getTripId(),
                        request.getEmployeeId(),
                        null
                );

        if (checkDriverAssignmentForTripExists) {
            throw new AppException(ErrorCode.DRIVER_ASSIGNMENT_FOR_TRIP_EXISTED);
        }

        DriverAssignmentForTrip driverAssignmentForTrip = new DriverAssignmentForTrip();

        driverAssignmentForTrip.setTrip(trip);
        driverAssignmentForTrip.setEmployee(employee);

        return driverAssignmentForTripRepository.save(driverAssignmentForTrip);
    }

    public List<DriverAssignmentForTrip> getDriverAssignmentForTrips()
    {
        return  driverAssignmentForTripRepository.findAll();
    }

    public DriverAssignmentForTripResponse getDriverAssignmentForTrip(String driverAssignmentForTripId)
    {
        return driverAssignmentForTripMapper
                .toDriverAssignmentForTripResponse(
                        driverAssignmentForTripRepository.findById(driverAssignmentForTripId)
                .orElseThrow(() -> new RuntimeException("Driver assignment for trip not found")));
    }

    public DriverAssignmentForTripResponse updateDriverAssignmentForTrip(
            DriverAssignmentForTripUpdateRequest request, String id)
    {
        DriverAssignmentForTrip driverAssignmentForTrip = driverAssignmentForTripRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Driver assignment for trip not found"));

        Trip trip = tripRepository
                .findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        boolean checkIsDriver = driverAssignmentForTripRepository.
                checkIsDriver(employee.getId());

//        Kiểm tra nhân viên có phải là tài xế không
        if (checkIsDriver) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_A_DRIVER);
        }

        boolean canDriverOperateVehicle = driverAssignmentForTripRepository.
                canDriverOperateVehicle(
                        request.getTripId(),
                        request.getEmployeeId()
                );
//        Kiểm tra tài xế có đc phân công phụ trách xe không
        if (canDriverOperateVehicle) {
            throw new AppException(ErrorCode.DRIVER_CAN_NOT_OPERATE_VEHICLE);
        }
//        Kiểm tra xem tài xế đã được phân công cho chuyến đi chưa
        boolean checkDriverAssignmentForTripExists = driverAssignmentForTripRepository.
                checkDriverAssignmentForTripExists(
                        request.getTripId(),
                        request.getEmployeeId(),
                        null
                );

        if (checkDriverAssignmentForTripExists) {
            throw new AppException(ErrorCode.DRIVER_ASSIGNMENT_FOR_TRIP_EXISTED);
        }

        driverAssignmentForTrip.setTrip(trip);
        driverAssignmentForTrip.setEmployee(employee);

        return driverAssignmentForTripMapper
                .toDriverAssignmentForTripResponse(
                        driverAssignmentForTripRepository.save(driverAssignmentForTrip)
                );
    }

    public DriverAssignmentForTripResponse patchUpdateDriverAssignmentForTrip(
            DriverAssignmentForTripUpdateRequest request, String id) {

        DriverAssignmentForTrip driverAssignmentForTrip = driverAssignmentForTripRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Driver assignment for trip not found"));

        if (request.getTripId() != null || request.getEmployeeId() != null)
        {
            Trip trip = (request.getTripId() != null)
                    ? tripRepository.findById(request.getTripId())
                    .orElseThrow(() -> new RuntimeException("Trip not found"))
                    : driverAssignmentForTrip.getTrip();

            Employee employee = (request.getEmployeeId() != null)
                    ? employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"))
                    : driverAssignmentForTrip.getEmployee();

            boolean checkIsDriver = driverAssignmentForTripRepository.
                    checkIsDriver(employee.getId());

//        Kiểm tra nhân viên có phải là tài xế không
            if (checkIsDriver) {
                throw new AppException(ErrorCode.EMPLOYEE_NOT_A_DRIVER);
            }

            boolean canDriverOperateVehicle = driverAssignmentForTripRepository.
                    canDriverOperateVehicle(
                            request.getTripId(),
                            request.getEmployeeId()
                    );

//        Kiểm tra tài xế có đc phân công phụ trách xe không
            if (canDriverOperateVehicle) {
                throw new AppException(ErrorCode.DRIVER_CAN_NOT_OPERATE_VEHICLE);
            }

//        Kiểm tra xem tài xế đã được phân công cho chuyến đi chưa
            boolean checkDriverAssignmentForTripExists = driverAssignmentForTripRepository.
                    checkDriverAssignmentForTripExists(
                            request.getTripId(),
                            request.getEmployeeId(),
                            null
                    );

            if (checkDriverAssignmentForTripExists) {
                throw new AppException(ErrorCode.DRIVER_ASSIGNMENT_FOR_TRIP_EXISTED);
            }

            driverAssignmentForTrip.setTrip(trip);
            driverAssignmentForTrip.setEmployee(employee);
        }

        return driverAssignmentForTripMapper.toDriverAssignmentForTripResponse(
                driverAssignmentForTripRepository.save(driverAssignmentForTrip));
    }

    public void deleteDriverAssignmentForTrip(String id) {
        driverAssignmentForTripRepository.findById(id)
                .map(driverAssignmentForTrip -> {
                    driverAssignmentForTripRepository.delete(driverAssignmentForTrip);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Driver assignment for trip not found for ID: " + id));
    }
}
