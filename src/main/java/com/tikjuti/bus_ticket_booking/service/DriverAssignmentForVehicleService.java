package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForVehicle.DriverAssignmentForVehicleCreationRequest;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        boolean checkDriverAssignmentForVehicleExists = driverAssignmentForVehicleRepository
                .checkDriverAssignmentForVehicleExists(
                        startDate.toString(),
                        endDate.toString(),
                        request.getVehicleId(),
                        request.getEmployeeId(),
                        null
                );
        if (checkDriverAssignmentForVehicleExists) {
            throw new AppException(ErrorCode.DRIVER_ASSIGNMENT_FOR_VEHICLE_EXISTED);
        }

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

//    public CustomerResponse updateCustomer(CustomerUpdateRequest request, String customerId)
//    {
//        Customer customer = customerRepository
//                .findById(customerId)
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        if(customerRepository.existsByEmail(request.getEmail()))
//            throw new AppException(ErrorCode.EMAIL_EXISTED);
//
//        if(customerRepository.existsByPhone(request.getPhone()))
//            throw new AppException(ErrorCode.PHONE_EXISTED);
//
//        LocalDate dob = LocalDate.parse(request.getDob().trim(), dateFormatter);
//
//        customer.setAddress(request.getAddress());
//        customer.setEmail(request.getEmail());
//        customer.setCustomerName(request.getCustomerName());
//        customer.setPhone(request.getPhone());
//        customer.setGender(request.getGender());
//        customer.setDob(dob);
//
//        return customerMapper
//                .toCustomerResponse(customerRepository.save(customer));
//    }
//
//    public CustomerResponse patchUpdateCustomer(CustomerUpdateRequest request, String customerId) {
//        Customer customer = customerRepository
//                .findById(customerId)
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        if (request.getEmail() != null) {
//            if(customerRepository.existsByEmail(request.getEmail()))
//                throw new AppException(ErrorCode.EMAIL_EXISTED);
//            customer.setEmail(request.getEmail());
//        }
//
//        if (request.getPhone() != null) {
//            if(customerRepository.existsByPhone(request.getPhone()))
//                throw new AppException(ErrorCode.PHONE_EXISTED);
//            customer.setPhone(request.getPhone());
//        }
//
//        if (request.getAddress() != null) {
//            customer.setAddress(request.getAddress());
//        }
//
//        if (request.getCustomerName() != null) {
//            customer.setCustomerName(request.getCustomerName());
//        }
//
//        if (request.getGender() != null) {
//            customer.setGender(request.getGender());
//        }
//
//        if (request.getDob() != null) {
//            LocalDate dob = LocalDate.parse(request.getDob().trim(), dateFormatter);
//            customer.setDob(dob);
//        }
//
//        return customerMapper.toCustomerResponse(customerRepository.save(customer));
//    }
//
//    public void deleteCustomer(String customerId) {
//        customerRepository.findById(customerId)
//                .map(customer -> {
//                    customerRepository.delete(customer);
//                    return true;
//                })
//                .orElseThrow(() -> new RuntimeException("Customer not found for ID: " + customerId));
//    }
}
