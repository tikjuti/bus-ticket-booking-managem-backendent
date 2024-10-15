
package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.Authencation.ChangePassword;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.repository.CustomerRepository;
import com.tikjuti.bus_ticket_booking.service.ForgotPasswordService;
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

    @PostMapping("/verifyOtp/{otp}/{email}")
    ResponseEntity<ApiResponse<Void>> verifyOtp(@PathVariable String email, @PathVariable Integer otp) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        forgotPasswordService.verifyOtp(email, otp);

        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setMessage("OTP verified!");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/changePassword/{email}")
    ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable String email, @RequestBody ChangePassword changePassword) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        forgotPasswordService.changePassword(email, changePassword);

        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setMessage("Password has been changed!");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
