package com.tikjuti.bus_ticket_booking.mapper;

import com.tikjuti.bus_ticket_booking.dto.request.PaymentMethod.PaymentMethodCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.PaymentMethod.PaymentMethodUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.PaymentMethodResponse;
import com.tikjuti.bus_ticket_booking.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {
    PaymentMethod toPaymentMethod(PaymentMethodCreationRequest request);

    PaymentMethodResponse toPaymentMethodResponse(PaymentMethod paymentMethod);

    void updatePaymentMethod(@MappingTarget PaymentMethod paymentMethod, PaymentMethodUpdateRequest request);
}
