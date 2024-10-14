
package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.response.AccountResponse;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.repository.CustomerRepository;
import com.tikjuti.bus_ticket_booking.service.AccountService;
import com.tikjuti.bus_ticket_booking.service.ForgotPasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/password")
public class ForgotPasswordController {
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/verifyEmail/{email}")
    ResponseEntity<ApiResponse<Void>> verifyEmail(@PathVariable String email) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        forgotPasswordService.verifyEmail(email);

        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setMessage("Email sent for verification!");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

//    @GetMapping
//    ResponseEntity<ApiResponse<List<Account>>> getAccounts()
//    {
//        List<Account> accountList = accountService.getAccounts();
//
//        ApiResponse<List<Account>> apiResponse = new ApiResponse<>();
//
//        apiResponse.setMessage("Accounts retrieved successfully");
//        apiResponse.setCode(HttpStatus.OK.value());
//        apiResponse.setResult(accountList);
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }


}
