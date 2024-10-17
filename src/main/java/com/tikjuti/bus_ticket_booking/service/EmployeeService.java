package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Employee.EmployeeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Employee.EmployeeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.EmployeeResponse;
import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.entity.Employee;
import com.tikjuti.bus_ticket_booking.entity.EmployeeType;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.EmployeeMapper;
import com.tikjuti.bus_ticket_booking.repository.AccountRepository;
import com.tikjuti.bus_ticket_booking.repository.EmployeeRepository;
import com.tikjuti.bus_ticket_booking.repository.EmployeeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PreAuthorize("hasRole('ADMIN')")
    public Employee createEmployee(EmployeeCreationRequest request)
    {
        if(employeeRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);

        if(employeeRepository.existsByPhone(request.getPhone()))
            throw new AppException(ErrorCode.PHONE_EXISTED);

        if(employeeRepository.existsByNationalIDNumber(request.getNationalIDNumber()))
            throw new AppException(ErrorCode.NATIONAL_ID_NUMBER_EXISTED);

        LocalDate dob = LocalDate.parse(request.getDob().trim(), dateFormatter);

        EmployeeType employeeType = employeeTypeRepository
                .findById(request.getEmployeeTypeId())
                .orElseThrow(() -> new RuntimeException("Employee type not found"));

        Employee employee = new Employee();

        employee.setAddress(request.getAddress());
        employee.setEmail(request.getEmail());
        employee.setEmployeeName(request.getEmployeeName());
        employee.setPhone(request.getPhone());
        employee.setGender(request.getGender());
        employee.setDob(dob);
        employee.setNationalIDNumber(request.getNationalIDNumber());
        employee.setStatus(request.getStatus().name());
        employee.setEmployeeType(employeeType);

        return employeeRepository.save(employee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Employee> getEmployees() {return  employeeRepository.findAll();}

    @PostAuthorize("returnObject.account.username == authentication.name || hasRole('ADMIN')")
    public EmployeeResponse getEmployee(String employeeId)
    {
        return employeeMapper
                .toEmployeeResponse(employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found")));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public EmployeeResponse updateEmployee(EmployeeUpdateRequest request, String employeeId)
    {
        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if(employeeRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);

        if(employeeRepository.existsByPhone(request.getPhone()))
            throw new AppException(ErrorCode.PHONE_EXISTED);

        if(employeeRepository.existsByNationalIDNumber(request.getNationalIDNumber()))
            throw new AppException(ErrorCode.NATIONAL_ID_NUMBER_EXISTED);

        LocalDate dob = LocalDate.parse(request.getDob().trim(), dateFormatter);

        EmployeeType employeeType = employeeTypeRepository
                .findById(request.getEmployeeTypeId())
                .orElseThrow(() -> new RuntimeException("Employee type not found"));

        Account account = accountRepository
                .findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        employee.setAddress(request.getAddress());
        employee.setEmail(request.getEmail());
        employee.setEmployeeName(request.getEmployeeName());
        employee.setPhone(request.getPhone());
        employee.setGender(request.getGender());
        employee.setDob(dob);
        employee.setNationalIDNumber(request.getNationalIDNumber());
        employee.setStatus(request.getStatus().name());
        employee.setEmployeeType(employeeType);
        employee.setAccount(account);

        return employeeMapper
                .toEmployeeResponse(employeeRepository.save(employee));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public EmployeeResponse patchUpdateEmployee(EmployeeUpdateRequest request, String employeeId) {
        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (request.getEmail() != null) {
            if(employeeRepository.existsByEmail(request.getEmail()))
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            employee.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) {
            if(employeeRepository.existsByPhone(request.getPhone()))
                throw new AppException(ErrorCode.PHONE_EXISTED);
            employee.setPhone(request.getPhone());
        }

        if (request.getNationalIDNumber() != null) {
            if(employeeRepository.existsByNationalIDNumber(request.getNationalIDNumber()))
                throw new AppException(ErrorCode.NATIONAL_ID_NUMBER_EXISTED);
            employee.setNationalIDNumber(request.getNationalIDNumber());
        }

        if (request.getEmployeeTypeId() != null) {
            EmployeeType employeeType = employeeTypeRepository
                    .findById(request.getEmployeeTypeId())
                    .orElseThrow(() -> new RuntimeException("Employee type not found"));
            employee.setEmployeeType(employeeType);
        }

        if (request.getAccountId() != null) {
            Account account = accountRepository
                    .findById(request.getAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            employee.setAccount(account);
        }

        if (request.getAddress() != null) {
            employee.setAddress(request.getAddress());
        }

        if (request.getEmployeeName() != null) {
            employee.setEmployeeName(request.getEmployeeName());
        }

        if (request.getGender() != null) {
            employee.setGender(request.getGender());
        }

        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus().name());
        }

        if (request.getDob() != null) {
            LocalDate dob = LocalDate.parse(request.getDob().trim(), dateFormatter);
            employee.setDob(dob);
        }

        return employeeMapper.toEmployeeResponse(employeeRepository.save(employee));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmployee(String employeeId) {
        employeeRepository.findById(employeeId)
                .map(employee -> {
                    employeeRepository.delete(employee);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Employee not found for ID: " + employeeId));
    }
}
