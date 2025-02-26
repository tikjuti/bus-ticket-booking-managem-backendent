package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.Utils.QueryableExtensions;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.MailBody;
import com.tikjuti.bus_ticket_booking.dto.request.Ticket.*;
import com.tikjuti.bus_ticket_booking.dto.response.BuyTicketResponse;
import com.tikjuti.bus_ticket_booking.dto.response.RouteResponse;
import com.tikjuti.bus_ticket_booking.dto.response.TicketResponse;
import com.tikjuti.bus_ticket_booking.entity.*;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.RouteMapper;
import com.tikjuti.bus_ticket_booking.mapper.TicketMapper;
import com.tikjuti.bus_ticket_booking.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    private VehicleRepository vehicleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private EmailService emailService;

    DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional
    public List<BuyTicketResponse> buyTicket(BuyTicketRequest request) {
        List<Object[]> trips = ticketRepository.findTripsByUser(
                request.getDepartureLocation(), request.getArrivalLocation(), request.getDepartureDate());

        List<BuyTicketResponse> buyTicketResponses = new ArrayList<>();

        for (Object[] trip : trips) {
            BuyTicketResponse buyTicketResponse = new BuyTicketResponse();

            java.sql.Date departureDateSql = (Date) trip[3];
            Time departureTimeSql = (Time) trip[4];
            java.sql.Date arrivalDateSql = (Date) trip[1];
            Time arrivalTimeSql = (Time) trip[2];

            LocalDate departureDate = departureDateSql.toLocalDate();
            LocalTime departureTime = departureTimeSql.toLocalTime();
            LocalDate arrivalDate = arrivalDateSql.toLocalDate();
            LocalTime arrivalTime = arrivalTimeSql.toLocalTime();

            buyTicketResponse.setTripId((String) trip[0]);
            buyTicketResponse.setDepartureDate(departureDate);
            buyTicketResponse.setDepartureTime(departureTime);
            buyTicketResponse.setArrivalDate(arrivalDate);
            buyTicketResponse.setArrivalTime(arrivalTime);

            Vehicle vehicle = vehicleRepository
                    .findById((String) trip[6])
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));

            buyTicketResponse.setVehicleId(vehicle.getId());
            buyTicketResponse.setVehicleName(vehicle.getVehicleName());
            buyTicketResponse.setTotalSeats(vehicle.getSeatCount());

            Route route = new Route();
            route.setId((String) trip[7]);
            route.setArrivalLocation((String) trip[8]);
            route.setArrivalPoint((String) trip[9]);
            route.setDepartureLocation((String) trip[10]);
            route.setDeparturePoint((String) trip[11]);
            route.setDistance((Integer) trip[12]);
            route.setDuration((Integer) trip[13]);

            RouteResponse routeResponse = routeMapper.toRouteResponse(route);

            buyTicketResponse.setRoute(routeResponse);

            buyTicketResponse.setTicketPrice(
                    ticketRepository.findTicketPrice(
                            (String) trip[6],
                            route.getId()));
            buyTicketResponse.setAvailableSeats(
                    ticketRepository.findAvailableSeatsByVehicleId(
                            (String) trip[6]));

            buyTicketResponses.add(buyTicketResponse);
        }

        return buyTicketResponses;
    }


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void sendInforTicketForUser() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        List<Trip> upcomingTrips = tripRepository.findStartingTrips(currentDate, currentTime);

        List<String> tripIds = upcomingTrips.stream().map(Trip::getId).collect(Collectors.toList());
        List<Ticket> tickets = ticketRepository.findByTripIdsAndEmailNotSent(tripIds);

        for (Ticket ticket : tickets) {
            log.warn("Gửi email cho vé ID: " + ticket.getId());
            String customerEmail = ticket.getCustomer().getEmail();
            if (customerEmail != null && !customerEmail.isEmpty()) {
                String emailContent = buildEmailContent(ticket);

                try {
                    MailBody mailBody = MailBody.builder()
                            .to(customerEmail)  // Địa chỉ email người nhận
                            .text(emailContent)  // Nội dung email và chỉ định 'true' để là HTML
                            .subject("Thông tin xác nhận vé của bạn")  // Tiêu đề email
                            .build();


                    emailService.sendSimpleMessage(mailBody);
                    ticket.setEmailSent(true);
                } catch (Exception e) {
                    System.err.println("Lỗi khi gửi email cho vé ID: " + ticket.getId());
                }
            }
        }

        ticketRepository.saveAll(tickets);

    }

    private String buildEmailContent(Ticket ticket) {
        return "Xin chào " + ticket.getCustomer().getCustomerName() + ",\n\n"
                + "Cảm ơn bạn đã đặt vé với chúng tôi! Dưới đây là thông tin xác nhận vé của bạn:\n\n"
                + "--------------------------------------------------\n"
                + "Mã vé          : " + ticket.getId() + "\n"
                + "Chuyến xe      : " + ticket.getTrip().getRoute().getDepartureLocation()
                + " - " + ticket.getTrip().getRoute().getArrivalLocation() + "\n"
                + "Ngày khởi hành : " + ticket.getTrip().getDepartureDate() + "\n"
                + "Giờ khởi hành  : " + ticket.getTrip().getDepartureTime() + "\n"
                + "Điểm đi        : " + ticket.getTrip().getRoute().getDeparturePoint() + "\n"
                + "Điểm đến       : " + ticket.getTrip().getRoute().getArrivalPoint() + "\n\n"
                + "Tên xe         : " + ticket.getTrip().getVehicle().getVehicleName() + "\n"
                + "Biển số        : " + ticket.getTrip().getVehicle().getLicensePlate() + "\n"
                + "Số ghế         : " + ticket.getSeat().getPosition() + "\n"
                + "Giá vé         : " + ticket.getActualTicketPrice() + " VND\n"
                + "--------------------------------------------------\n\n"
                + "Chúc bạn có một chuyến đi an toàn và thoải mái!\n\n"
                + "Trân trọng,\n"
                + "Đội ngũ hỗ trợ";
    }



    public BuyTicketResponse buyTicketById(String tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new RuntimeException("Trip not found"));

        BuyTicketResponse buyTicketResponse = new BuyTicketResponse();

        buyTicketResponse.setTripId(trip.getId());
        buyTicketResponse.setDepartureDate(trip.getDepartureDate());
        buyTicketResponse.setDepartureTime(trip.getDepartureTime());
        buyTicketResponse.setArrivalDate(trip.getArrivalDate());
        buyTicketResponse.setArrivalTime(trip.getArrivalTime());

        Vehicle vehicle = vehicleRepository
                .findById((trip.getVehicle().getId()))
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        buyTicketResponse.setVehicleId(vehicle.getId());
        buyTicketResponse.setVehicleName(vehicle.getVehicleName());
        buyTicketResponse.setTotalSeats(vehicle.getSeatCount());


        Route route = routeRepository
                .findById(trip.getRoute().getId()).orElseThrow(() -> new RuntimeException("Route not found"));

        RouteResponse routeResponse = routeMapper.toRouteResponse(route);

        buyTicketResponse.setRoute(routeResponse);

        buyTicketResponse.setTicketPrice(
                ticketRepository.findTicketPrice(
                        trip.getVehicle().getId(),
                        route.getId()));
        buyTicketResponse.setAvailableSeats(
                ticketRepository.findAvailableSeatsByVehicleId(
                        vehicle.getId()));

        return buyTicketResponse;
    }


    public List<Ticket> getTicketsByTripId(String tripId) {
        return ticketRepository.findByTripId(tripId);
    }

    public Ticket createTicket(TicketCreationRequest request)
    {
        Ticket ticket = new Ticket();

        Customer customer;
        if (request.getCustomerId() == null) {

            if (customerRepository.findByEmailOrPhone(request.getEmail(), request.getPhone()) != null) {
                customer = customerRepository.findByEmailOrPhone(request.getEmail(), request.getPhone());
            } else {
                customer = new Customer();
                customer.setCustomerName(request.getCustomerName());
                customer.setPhone(request.getPhone());
                customer.setEmail(request.getEmail());

                customerRepository.save(customer);
            }
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

        Ticket ticketCreated = ticketRepository.save(ticket);
        String emailContent = buildEmailContentCreateTicket(ticketCreated);

        try {
            MailBody mailBody = MailBody.builder()
                    .to(customer.getEmail())  // Địa chỉ email người nhận
                    .text(emailContent)  // Nội dung email và chỉ định 'true' để là HTML
                    .subject("Thông tin vé của bạn")  // Tiêu đề email
                    .build();


            emailService.sendSimpleMessage(mailBody);
            ticket.setEmailSent(false);
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email cho vé ID: " + ticket.getId());
        }

        return ticketCreated;
    }

    private String buildEmailContentCreateTicket(Ticket ticket) {
        return "Xin chào " + ticket.getCustomer().getCustomerName() + ",\n\n"
                + "Cảm ơn bạn đã đặt vé với chúng tôi! Dưới đây là thông tin vé của bạn:\n\n"
                + "--------------------------------------------------\n"
                + "Mã vé          : " + ticket.getId() + "\n"
                + "Chuyến xe      : " + ticket.getTrip().getRoute().getDepartureLocation()
                + " - " + ticket.getTrip().getRoute().getArrivalLocation() + "\n"
                + "Ngày khởi hành : " + ticket.getTrip().getDepartureDate() + "\n"
                + "Giờ khởi hành  : " + ticket.getTrip().getDepartureTime() + "\n"
                + "Điểm đi        : " + ticket.getTrip().getRoute().getDeparturePoint() + "\n"
                + "Điểm đến       : " + ticket.getTrip().getRoute().getArrivalPoint() + "\n\n"
                + "Tên xe         : " + ticket.getTrip().getVehicle().getVehicleName() + "\n"
                + "Số ghế         : " + ticket.getSeat().getPosition() + "\n"
                + "Giá vé         : " + ticket.getActualTicketPrice() + " VND\n"
                + "--------------------------------------------------\n\n"
                + "Trân trọng,\n"
                + "Đội ngũ hỗ trợ";
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public PaginatedResult<Ticket> getTickets(TicketQueryRequest queryRequest)
    {
        Map<String, Object> filterParams = new HashMap<>();

        if (queryRequest.getId() != null)
            filterParams.put("id", queryRequest.getId());

        if (queryRequest.getActualTicketPrice() != null)
            filterParams.put("actualTicketPrice", queryRequest.getActualTicketPrice());

        if (queryRequest.getTicketStatus() != null)
            filterParams.put("ticketStatus", queryRequest.getTicketStatus());

        if (queryRequest.getBookingTime() != null)
            filterParams.put("bookingTime", queryRequest.getBookingTime());

        if (queryRequest.getStatusPayment() != null)
            filterParams.put("statusPayment", queryRequest.getStatusPayment());

        if (queryRequest.getPaymentDate() != null)
            filterParams.put("paymentDate", queryRequest.getPaymentDate());

        Specification<Ticket> spec = Specification.where(
                        QueryableExtensions.<Ticket>applyIncludes(queryRequest.getIncludes()))
                .and(QueryableExtensions.applyFilters(filterParams))
                .and(QueryableExtensions.applySorting(queryRequest.getSort()));

        return QueryableExtensions.applyPagination(
                ticketRepository, spec, queryRequest.getPage(), queryRequest.getPageSize());
    }

    public TicketResponse getTicket(String ticketId) {
        return ticketMapper
                .toTicketResponse(
                        ticketRepository.findById(ticketId)
                                .orElseThrow(() -> new RuntimeException("Ticket not found")));
    }

    public TicketResponse getTicketByTicketIdAndPhone(LookUpTicketRequest request) {
        return ticketMapper
                .toTicketResponse(
                        ticketRepository.findTicketByTicketIdAndPhone(request.getTicketId(), request.getPhone())
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

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTicket(String id) {
        ticketRepository.findById(id)
                .map(ticket -> {
                    ticketRepository.delete(ticket);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Ticket not found for ID: " + id));
    }

}
