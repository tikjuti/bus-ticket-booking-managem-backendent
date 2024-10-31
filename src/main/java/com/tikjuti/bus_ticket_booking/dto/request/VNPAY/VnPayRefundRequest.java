package com.tikjuti.bus_ticket_booking.dto.request.VNPAY;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VnPayRefundRequest {
    String transactionType;
    String orderId;
    String amount;
    String transactionDate;
    String createdBy;
}
