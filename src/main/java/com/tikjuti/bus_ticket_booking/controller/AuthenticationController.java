
package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Account.AuthenticationRequest;
import com.tikjuti.bus_ticket_booking.dto.response.AccountResponse;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.AuthenticationResponse;
import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.service.AccountService;
import com.tikjuti.bus_ticket_booking.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();

        boolean isAuthenticated = authenticationService.authenticate(request);

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Account created successfully");
        apiResponse.setResult(new AuthenticationResponse(isAuthenticated));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
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
//
//    @GetMapping("/{accountId}")
//    ResponseEntity<ApiResponse<AccountResponse>> getRoute(@PathVariable String accountId) {
//
//        ApiResponse<AccountResponse> apiResponse = new ApiResponse<>();
//
//        apiResponse.setMessage("Account retrieved successfully");
//        apiResponse.setCode(HttpStatus.OK.value());
//        apiResponse.setResult(accountService.getAccount(accountId));
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
}
