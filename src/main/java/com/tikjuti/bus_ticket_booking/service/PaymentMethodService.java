package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.PaymentMethod.PaymentMethodCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.PaymentMethod.PaymentMethodUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.PaymentMethodResponse;
import com.tikjuti.bus_ticket_booking.entity.PaymentMethod;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.PaymentMethodMapper;
import com.tikjuti.bus_ticket_booking.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    public PaymentMethod createPaymentMethod(PaymentMethodCreationRequest request)
    {
        if(paymentMethodRepository.existsByMethodName(request.getMethodName()))
            throw new AppException(ErrorCode.PAYMENT_METHOD_EXISTED);

        PaymentMethod paymentMethod = paymentMethodMapper.toPaymentMethod(request);

        return paymentMethodRepository
                .save(paymentMethod);
    }

    public List<PaymentMethod> getPaymentMethods() {return  paymentMethodRepository.findAll();}

    public PaymentMethodResponse getPaymentMethod(String paymentMethodId)
    {
        return paymentMethodMapper
                .toPaymentMethodResponse(paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("Payment method not found")));
    }

    public PaymentMethodResponse updatePaymentMethod(PaymentMethodUpdateRequest request, String paymentMethodId)
    {
        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        if(paymentMethodRepository.existsByMethodName(request.getMethodName()))
            throw new AppException(ErrorCode.PAYMENT_METHOD_EXISTED);

        paymentMethodMapper.updatePaymentMethod(paymentMethod, request);

        return paymentMethodMapper
                .toPaymentMethodResponse(paymentMethodRepository.save(paymentMethod));
    }

    public void deletePaymentMethod(String paymentMethodId) {
        paymentMethodRepository.findById(paymentMethodId)
                .map(paymentMethod -> {
                    paymentMethodRepository.delete(paymentMethod);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Payment method not found for ID: " + paymentMethodId));
    }
}
