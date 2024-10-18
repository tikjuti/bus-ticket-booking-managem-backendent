package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.ZaloPay.CallbackRequest;
import com.tikjuti.bus_ticket_booking.dto.request.ZaloPay.ZaloPayPaymentRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ZaloPayPaymentResponse;
import com.tikjuti.bus_ticket_booking.service.ZaloPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zaloPayment")
public class ZaloPayPaymentController {
    @Autowired
    private ZaloPayService zaloPayService;

    @PostMapping("/create")
    public ZaloPayPaymentResponse createPayment(@RequestBody ZaloPayPaymentRequest request) throws Exception {

        return zaloPayService.createPayment(request);

    }

    @PostMapping("/callback")
    public String callBack(@RequestBody CallbackRequest request) throws Exception {
        return zaloPayService.callBack(request);
    }
}
