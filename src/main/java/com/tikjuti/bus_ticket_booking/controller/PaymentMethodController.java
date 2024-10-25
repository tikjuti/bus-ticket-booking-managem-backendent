package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.dto.request.PaymentMethod.PaymentMethodCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.PaymentMethod.PaymentMethodQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.PaymentMethod.PaymentMethodUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.PaymentMethodResponse;
import com.tikjuti.bus_ticket_booking.entity.PaymentMethod;
import com.tikjuti.bus_ticket_booking.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paymentMethods")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping
    ResponseEntity<ApiResponse<PaymentMethod>> createPaymentMethod(@RequestBody PaymentMethodCreationRequest request)
    {
        ApiResponse<PaymentMethod> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Payment method created successfully");
        apiResponse.setResult(paymentMethodService.createPaymentMethod(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @GetMapping
    ResponseEntity<ApiResponse<PaginatedResult<PaymentMethod>>> getPaymentMethods(
            PaymentMethodQueryRequest queryRequest)
    {
        PaginatedResult<PaymentMethod> paymentMethodList = paymentMethodService.getPaymentMethods(queryRequest);

        ApiResponse<PaginatedResult<PaymentMethod>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Payment methods retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(paymentMethodList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }


    @GetMapping("/{paymentMethodId}")
    ResponseEntity<ApiResponse<PaymentMethodResponse>> getPaymentMethod(@PathVariable String paymentMethodId) {

        ApiResponse<PaymentMethodResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Payment method retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(paymentMethodService.getPaymentMethod(paymentMethodId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @PutMapping("/{paymentMethodId}")
    ResponseEntity<ApiResponse<PaymentMethodResponse>> updatePaymentMethod(@PathVariable String paymentMethodId,
                                                                           @RequestBody PaymentMethodUpdateRequest request)
    {
        ApiResponse<PaymentMethodResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Payment method update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(paymentMethodService.updatePaymentMethod(request, paymentMethodId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @DeleteMapping("/{paymentMethodId}")
    ResponseEntity<Void> deletePaymentMethod(@PathVariable String paymentMethodId) {

            paymentMethodService.deletePaymentMethod(paymentMethodId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
