package com.tikjuti.bus_ticket_booking.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoMoPaymentResponse {
    String partnerCode;
    String requestId;
    String orderId;
    Long amount;
    Long responseTime;
    String message;
    Integer resultCode;
    String payUrl;

    public MoMoPaymentResponse() {
        this.responseTime = Instant.now().toEpochMilli();
    }

}
