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
    String partnerCode = "MOMO";
    String partnerName = "Test";
    String storeId = "BusTicketBooking"; ;
    String requestId;
    Long amount;
    String orderId ;
    String orderInfo ;
    String redirectUrl = "https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b";
    String ipnUrl = "https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b";
    String lang = "vi";
    String requestType = "payWithMethod";
    Boolean autoCapture = true;
    String extraData = "" ;
    String signature;
}
