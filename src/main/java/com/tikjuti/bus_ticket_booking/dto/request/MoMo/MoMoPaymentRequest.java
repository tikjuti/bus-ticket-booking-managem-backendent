package com.tikjuti.bus_ticket_booking.dto.request.MoMo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MoMoPaymentRequest {
    String orderId ;
    String orderInfo ;
    String routeName;
    String departureTime;
    String departureDate;
    String departurePoint;
    String customerName;
    String phone;
    String email = "";
    Long amount;
    String extraData = "" ;
}
