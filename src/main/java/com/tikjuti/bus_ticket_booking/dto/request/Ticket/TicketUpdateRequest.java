package com.tikjuti.bus_ticket_booking.dto.request.Ticket;

import com.tikjuti.bus_ticket_booking.enums.PaymentStatus;
import com.tikjuti.bus_ticket_booking.enums.TicketStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketUpdateRequest {
    int actualTicketPrice;
    TicketStatus ticketStatus;
    String bookingTime;
    PaymentStatus statusPayment;
    String paymentDate;
    String customerId;
    String tripId;
    String paymentMethodId;
    String seatId;
    String employeeId;
}
