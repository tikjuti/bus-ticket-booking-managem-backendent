package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.VNPAY.VnPayQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.VNPAY.VnPayRefundRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<String>> createPayment(HttpServletRequest request,
                                                             @RequestParam("order_id") String orderId,
                                                             @RequestParam("amount") String amount) {
        try {
            request.setAttribute("amount", amount);
            request.setAttribute("order_id", orderId);

            ApiResponse<String> paymentUrl = vnPayService.createPayment(request);
            return ResponseEntity.ok(paymentUrl);

        } catch (UnsupportedEncodingException e) {
            log.error("Error creating payment URL: {}", e.getMessage(), e);

            // Trả về lỗi với thông báo chi tiết bọc trong ApiResponse
            ApiResponse<String> errorResponse =
                    new ApiResponse<>(404, "Error: Unable to create payment URL. Details: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);

            // Trả về lỗi chung với thông báo chi tiết bọc trong ApiResponse
            ApiResponse<String> errorResponse =
                    new ApiResponse<>(404, "Unexpected error occurred: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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
