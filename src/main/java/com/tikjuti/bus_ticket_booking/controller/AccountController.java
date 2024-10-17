
package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.AccountResponse;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    ResponseEntity<ApiResponse<Account>> createAccount(@RequestBody @Valid AccountCreationRequest request) {
        ApiResponse<Account> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Account created successfully");
        apiResponse.setResult(accountService.createAccount(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<Account>>> getAccounts()
    {
        List<Account> accountList = accountService.getAccounts();

        ApiResponse<List<Account>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Accounts retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(accountList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    ResponseEntity<ApiResponse<AccountResponse>> getRoute(@PathVariable String accountId) {

        ApiResponse<AccountResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Account retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(accountService.getAccount(accountId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{accountId}")
    ResponseEntity<ApiResponse<AccountResponse>> updateAccount(@PathVariable String accountId, @RequestBody AccountUpdateRequest request)
    {
        ApiResponse<AccountResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Account update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(accountService.updateAccount(request, accountId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{accountId}")
    ResponseEntity<ApiResponse<AccountResponse>> updatePatchAccount(@PathVariable String accountId, @RequestBody AccountUpdateRequest request)
    {
        ApiResponse<AccountResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Account update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(accountService.patchUpdateAccount(request, accountId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {

        accountService.deleteAccount(accountId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
