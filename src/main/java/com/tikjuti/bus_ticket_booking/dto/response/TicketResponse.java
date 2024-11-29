package com.tikjuti.bus_ticket_booking.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tikjuti.bus_ticket_booking.entity.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketResponse {
    String id;
    int actualTicketPrice;
    String ticketStatus;
    LocalTime bookingTime;
    String statusPayment;
    LocalDate paymentDate;
    CustomerResponse customer;
    TripResponse trip;
    PaymentMethodResponse paymentMethod;
    SeatResponse seatId;
    EmployeeResponse employee;
}
