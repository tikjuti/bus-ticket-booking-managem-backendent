package com.tikjuti.bus_ticket_booking.dto.request.Ticket;

import com.tikjuti.bus_ticket_booking.enums.PaymentStatus;
import com.tikjuti.bus_ticket_booking.enums.TicketStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketCreationRequest {
    int actualTicketPrice;
    TicketStatus ticketStatus = TicketStatus.CONFIRMED;
    String bookingTime = LocalTime.now().toString();
    PaymentStatus statusPayment = PaymentStatus.PAID;
    String paymentDate = LocalDate.now().toString();
    String customerId;
    String tripId;
    String paymentMethodId;
    String seatId;
    String employeeId;

    String customerName;
    String phone;
    String email;
}
