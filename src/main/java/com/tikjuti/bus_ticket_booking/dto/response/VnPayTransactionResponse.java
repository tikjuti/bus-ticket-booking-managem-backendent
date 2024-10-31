package com.tikjuti.bus_ticket_booking.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VnPayTransactionResponse {
    String vnp_ResponseId;
    String vnp_Command;
    String vnp_ResponseCode;
    String vnp_Message;
    String vnp_TmnCode;
    String vnp_TxnRef;
    String vnp_Amount;
    String vnp_OrderInfo;
    String vnp_BankCode;
    String vnp_PayDate;
    String vnp_TransactionNo;
    String vnp_TransactionType;
    String vnp_TransactionStatus;
    String vnp_SecureHash;
}
