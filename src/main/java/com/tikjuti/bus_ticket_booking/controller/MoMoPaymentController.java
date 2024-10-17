package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.MoMo.MoMoPaymentRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.MoMoPaymentResponse;
import com.tikjuti.bus_ticket_booking.service.MoMoPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/momoPayment")
public class MoMoPaymentController {
    @Autowired
    private MoMoPaymentService moMoPaymentService;

    @PostMapping("/create")
    public MoMoPaymentResponse createPayment(@RequestBody MoMoPaymentRequest request) throws Exception {

        return moMoPaymentService.createPayment(request);

    }

}
