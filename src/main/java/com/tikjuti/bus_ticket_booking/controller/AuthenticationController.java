
package com.tikjuti.bus_ticket_booking.controller;

import com.nimbusds.jose.JOSEException;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.AuthenticationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.IntrospectRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.AuthenticationResponse;
import com.tikjuti.bus_ticket_booking.dto.response.IntrospectResponse;
import com.tikjuti.bus_ticket_booking.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/token")
    ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) throws JOSEException {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();

        var isAuthenticated = authenticationService.authenticate(request);

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Login successfully");
        apiResponse.setResult(AuthenticationResponse.builder()
                .token(isAuthenticated.getToken())
                .authenticated(isAuthenticated.isAuthenticated())
                .build());

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/introspect")
    ResponseEntity<ApiResponse<IntrospectResponse>> authenticate(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        ApiResponse<IntrospectResponse> apiResponse = new ApiResponse<>();

        var isAuthenticated = authenticationService.introspect(request);

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Introspect successfully");
        apiResponse.setResult(IntrospectResponse.builder()
                .valid(isAuthenticated.isValid())
                .build());

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
