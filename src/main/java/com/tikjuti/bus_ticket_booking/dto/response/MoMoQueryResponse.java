package com.tikjuti.bus_ticket_booking.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoMoQueryResponse {
    String partnerCode;
    String requestId;
    String orderId;
    String extraData;
    Long amount;
    Long transId;
    String payType;
    Integer resultCode;
    List refundTrans;
    String message;
    Long responseTime;

    public MoMoQueryResponse() {
        this.responseTime = Instant.now().toEpochMilli();
    }

}
