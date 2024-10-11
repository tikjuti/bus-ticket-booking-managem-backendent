package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Ticket.TicketCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Ticket.TicketUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.TicketResponse;
import com.tikjuti.bus_ticket_booking.entity.*;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.TicketMapper;
import com.tikjuti.bus_ticket_booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TicketMapper ticketMapper;

    DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Ticket createTicket(TicketCreationRequest request)
    {
        Ticket ticket = new Ticket();

        Customer customer;
        if (request.getEmployeeId() == null) {
            customer = new Customer();
            customer.setCustomerName(request.getCustomerName());
            customer.setPhone(request.getPhone());
            customer.setEmail(request.getEmail());

            customerRepository.save(customer);
        } else {
            customer = customerRepository
                    .findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
        }
        ticket.setCustomer(customer);

        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository
                    .findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            boolean checkIsEmployeeBooking = ticketRepository.
                    checkIsEmployeeBooking(employee.getId());

            if (checkIsEmployeeBooking) {
                throw new AppException(ErrorCode.EMPLOYEE_NOT_A_BOOKING);
            }
            ticket.setEmployee(employee);
        }

        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        Seat seat = seatRepository
                .findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        Trip trip = tripRepository
                .findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        LocalTime bookingTime = LocalTime.parse(request.getBookingTime().trim(), timeFormatter);

        if (request.getStatusPayment().name().equals("PAID")) {
            LocalDate paymentDate = LocalDate.parse(request.getPaymentDate().trim(), dateFormatter);
            ticket.setPaymentDate(paymentDate);
        }

        ticket.setActualTicketPrice(request.getActualTicketPrice());
        ticket.setTicketStatus(request.getTicketStatus().name());
        ticket.setBookingTime(bookingTime);
        ticket.setStatusPayment(request.getStatusPayment().name());
        ticket.setPaymentMethod(paymentMethod);
        ticket.setSeat(seat);
        ticket.setTrip(trip);

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTickets()
    {
        return  ticketRepository.findAll();
    }

    public TicketResponse getTicket(String ticketId) {
        return ticketMapper
                .toTicketResponse(
                        ticketRepository.findById(ticketId)
                                .orElseThrow(() -> new RuntimeException("Ticket not found")));
    }


    public TicketResponse updateTicket(TicketUpdateRequest request, String id)
    {
        Ticket ticket = ticketRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository
                    .findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            boolean checkIsEmployeeBooking = ticketRepository.
                    checkIsEmployeeBooking(employee.getId());

            if (checkIsEmployeeBooking) {
                throw new AppException(ErrorCode.EMPLOYEE_NOT_A_BOOKING);
            }
            ticket.setEmployee(employee);
        }

        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        Seat seat = seatRepository
                .findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        Trip trip = tripRepository
                .findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        LocalTime bookingTime = LocalTime.parse(request.getBookingTime().trim(), timeFormatter);

        if (request.getStatusPayment().name().equals("PAID")) {
            LocalDate paymentDate = LocalDate.parse(request.getPaymentDate().trim(), dateFormatter);
            ticket.setPaymentDate(paymentDate);
        }

        ticket.setActualTicketPrice(request.getActualTicketPrice());
        ticket.setTicketStatus(request.getTicketStatus().name());
        ticket.setBookingTime(bookingTime);
        ticket.setStatusPayment(request.getStatusPayment().name());
        ticket.setPaymentMethod(paymentMethod);
        ticket.setSeat(seat);
        ticket.setTrip(trip);
        ticket.setCustomer(customer);

        return ticketMapper
                .toTicketResponse(
                        ticketRepository.save(ticket)
                );
    }

    public TicketResponse patchUpdateTicket(
            TicketUpdateRequest request, String id) {

        Ticket ticket = ticketRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (request.getCustomerId() != null) {
            Customer customer = customerRepository
                    .findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            ticket.setCustomer(customer);
        }

        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository
                    .findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            boolean checkIsEmployeeBooking = ticketRepository.
                    checkIsEmployeeBooking(employee.getId());

            if (checkIsEmployeeBooking) {
                throw new AppException(ErrorCode.EMPLOYEE_NOT_A_BOOKING);
            }
            ticket.setEmployee(employee);
        }

        if (request.getPaymentMethodId() != null) {
            PaymentMethod paymentMethod = paymentMethodRepository
                    .findById(request.getPaymentMethodId())
                    .orElseThrow(() -> new RuntimeException("Payment method not found"));
            ticket.setPaymentMethod(paymentMethod);
        }

        if (request.getSeatId() != null) {
            Seat seat = seatRepository
                    .findById(request.getSeatId())
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            ticket.setSeat(seat);
        }

        if (request.getTripId() != null) {
            Trip trip = tripRepository
                    .findById(request.getTripId())
                    .orElseThrow(() -> new RuntimeException("Trip not found"));
            ticket.setTrip(trip);
        }

        if (request.getBookingTime() != null) {
            LocalTime bookingTime = LocalTime.parse(request.getBookingTime().trim(), timeFormatter);
            ticket.setBookingTime(bookingTime);
        }

        if (request.getPaymentDate() != null) {
            LocalDate paymentDate = LocalDate.parse(request.getPaymentDate().trim(), dateFormatter);
            ticket.setPaymentDate(paymentDate);
        }

        if (request.getActualTicketPrice() != 0) {
            ticket.setActualTicketPrice(request.getActualTicketPrice());
        }

        if (request.getTicketStatus() != null) {
            ticket.setTicketStatus(request.getTicketStatus().name());
        }

        if (request.getStatusPayment() != null) {
            ticket.setStatusPayment(request.getStatusPayment().name());
        }

        return ticketMapper.toTicketResponse(
                ticketRepository.save(ticket));
    }

    public void deleteTicket(String id) {
        ticketRepository.findById(id)
                .map(ticket -> {
                    ticketRepository.delete(ticket);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Ticket not found for ID: " + id));
    }
}
