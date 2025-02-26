package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.Utils.QueryableExtensions;
import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Customer.CustomerCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Customer.CustomerQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Customer.CustomerUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.CustomerResponse;
import com.tikjuti.bus_ticket_booking.entity.*;
import com.tikjuti.bus_ticket_booking.enums.AccountRole;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.CustomerMapper;
import com.tikjuti.bus_ticket_booking.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private AccountService accountService;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Customer createCustomer(CustomerCreationRequest request)
    {
        if(customerRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);

        if(customerRepository.existsByPhone(request.getPhone()))
            throw new AppException(ErrorCode.PHONE_EXISTED);

        AccountCreationRequest requestAccount = new AccountCreationRequest();
        requestAccount.setUsername(request.getUsername());
        requestAccount.setPassword(request.getPassword());

        HashSet<AccountRole> roles = new HashSet<>();
        roles.add(AccountRole.GUEST);

        requestAccount.setRoles(roles);

        Account account = accountService.createAccount(requestAccount);

        LocalDate dob = LocalDate.parse(request.getDob().trim(), dateFormatter);

        Customer customer = new Customer();

        customer.setAccount(account);
        customer.setAddress(request.getAddress());
        customer.setEmail(request.getEmail());
        customer.setCustomerName(request.getCustomerName());
        customer.setPhone(request.getPhone());
        customer.setGender(request.getGender());
        customer.setDob(dob);

        return customerRepository.save(customer);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public PaginatedResult<Customer> getCustomers(CustomerQueryRequest queryRequest) {

        Map<String, Object> filterParams = new HashMap<>();

        if (queryRequest.getId() != null) {
            filterParams.put("id", queryRequest.getId());
        }

        if (queryRequest.getCustomerName() != null) {
            filterParams.put("customerName", queryRequest.getCustomerName());
        }

        if (queryRequest.getPhone() != null) {
            filterParams.put("phone", queryRequest.getPhone());
        }

        if (queryRequest.getEmail() != null) {
            filterParams.put("email", queryRequest.getEmail());
        }

        Specification<Customer> spec = Specification.where(QueryableExtensions.<Customer>applyIncludes(queryRequest.getIncludes()))
                .and(QueryableExtensions.applyFilters(filterParams))
                .and(QueryableExtensions.applySorting(queryRequest.getSort()));

        return QueryableExtensions.applyPagination(customerRepository, spec, queryRequest.getPage(), queryRequest.getPageSize());
    }

    @PostAuthorize("returnObject.account.username == authentication.name || hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public CustomerResponse getCustomer(String customerId)
    {
        return customerMapper
                .toCustomerResponse(customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found")));
    }

    public CustomerResponse updateCustomer(CustomerUpdateRequest request, String customerId)
    {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if(customerRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);

        if(customerRepository.existsByPhone(request.getPhone()))
            throw new AppException(ErrorCode.PHONE_EXISTED);

        LocalDate dob = LocalDate.parse(request.getDob().trim(), dateFormatter);

        customer.setAddress(request.getAddress());
        customer.setEmail(request.getEmail());
        customer.setCustomerName(request.getCustomerName());
        customer.setPhone(request.getPhone());
        customer.setGender(request.getGender());
        customer.setDob(dob);

        return customerMapper
                .toCustomerResponse(customerRepository.save(customer));
    }

    public CustomerResponse patchUpdateCustomer(CustomerUpdateRequest request, String customerId) {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (request.getEmail() != null) {
            if(customerRepository.existsByEmail(request.getEmail()))
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            customer.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) {
            if(customerRepository.existsByPhone(request.getPhone()))
                throw new AppException(ErrorCode.PHONE_EXISTED);
            customer.setPhone(request.getPhone());
        }

        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }

        if (request.getCustomerName() != null) {
            customer.setCustomerName(request.getCustomerName());
        }

        if (request.getGender() != null) {
            customer.setGender(request.getGender());
        }

        if (request.getDob() != null) {
            LocalDate dob = LocalDate.parse(request.getDob().trim(), dateFormatter);
            customer.setDob(dob);
        }

        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public void deleteCustomer(String customerId) {
        customerRepository.findById(customerId)
                .map(customer -> {
                    customerRepository.delete(customer);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Customer not found for ID: " + customerId));
    }
}
