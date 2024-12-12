
package com.tikjuti.bus_ticket_booking.controller;

import com.nimbusds.jose.JOSEException;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.AuthenticationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.IntrospectRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.LogoutRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.AuthenticationResponse;
import com.tikjuti.bus_ticket_booking.dto.response.IntrospectResponse;
import com.tikjuti.bus_ticket_booking.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.ParseException;
import java.util.Map;

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

    @GetMapping("/social-login")
    ResponseEntity<Void> socialLogin(@RequestParam String loginType) throws UnsupportedEncodingException {

        loginType = loginType.trim().toLowerCase();

        String url = authenticationService.socialLogin(loginType);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/google/callback")
    ResponseEntity<ApiResponse<AuthenticationResponse>>  handleGoogleCallback(@RequestParam String code, @RequestParam String loginType) throws JOSEException {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();

        var isAuthenticated = authenticationService.handleGoogleCallback(code, loginType);

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Login successfully");
        apiResponse.setResult(AuthenticationResponse.builder()
                .token(isAuthenticated.getToken())
                .authenticated(isAuthenticated.isAuthenticated())
                .build());

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/facebook/callback")
    ResponseEntity<ApiResponse<AuthenticationResponse>>  handleFacebookCallback(@RequestParam String code, @RequestParam String loginType) throws JOSEException {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();

        var isAuthenticated = authenticationService.handleFacebookCallback(code, loginType);

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

    @PostMapping("/logout")
    ResponseEntity<ApiResponse<Void>> logout(@RequestBody LogoutRequest request)
            throws JOSEException, ParseException {

        ApiResponse<Void> apiResponse = new ApiResponse<>();

        authenticationService.logout(request);

        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setMessage("Logout successfully");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
