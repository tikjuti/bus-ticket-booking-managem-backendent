package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.VNPAY.VnPayQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.VNPAY.VnPayRefundRequest;
import com.tikjuti.bus_ticket_booking.dto.response.VnPayTransactionResponse;
import com.tikjuti.bus_ticket_booking.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/api/vnpay")
public class VnPayPaymentController {
    @Autowired
    private VnPayService vnPayService;

    @GetMapping("/create")
    public ResponseEntity<String> createPayment(HttpServletRequest request, @RequestParam("amount") String amount) {
        try {
            request.setAttribute("amount", amount);

            String paymentUrl = vnPayService.createPayment(request).toString();
            return ResponseEntity.ok(paymentUrl);

        } catch (UnsupportedEncodingException e) {
            log.error("Error creating payment URL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Unable to create payment URL. Details: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/query")
    public VnPayTransactionResponse queryPayment(@RequestBody VnPayQueryRequest request) throws IOException {
             return vnPayService.queryTransaction(request);
    }

    @PostMapping("/refund")
    public VnPayTransactionResponse refundPayment(@RequestBody VnPayRefundRequest request) throws IOException {
        return vnPayService.refundTransactions(request);
    }
}
