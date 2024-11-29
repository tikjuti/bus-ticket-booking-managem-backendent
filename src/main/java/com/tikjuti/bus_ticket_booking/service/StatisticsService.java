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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatisticsService {
    @Autowired
    private TicketRepository ticketRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public List<List<Map<Object, Object>>> getStatisticsByDateRange(String type, LocalDate startDate, LocalDate endDate) {
        List<Object[]> data;
        switch (type.toLowerCase()) {
            case "day":
                data = ticketRepository.countTicketsByDay(startDate, endDate);
                break;
            case "route":
                data = ticketRepository.countTicketsByRoute(startDate, endDate);
                break;
            case "month":
                data = ticketRepository.countTicketsByMonth(startDate, endDate);
                break;
            case "year":
                data = ticketRepository.countTicketsByYear(startDate, endDate);
                break;
            default:
                throw new IllegalArgumentException("Invalid type for statistics: " + type);
        }

        List<Map<Object, Object>> dataPoints = new ArrayList<>();
        if (type.equals("month")) {
            // Convert data into the required static structure
            for (Object[] row : data) {
                Map<Object, Object> map = new HashMap<>();
                map.put("x",  row[1].toString() +"/"+ row[0].toString());
                map.put("y", row[2]);
                dataPoints.add(map);
            }
        } else {
            // Convert data into the required static structure
            for (Object[] row : data) {
                Map<Object, Object> map = new HashMap<>();
                map.put("x", row[1]);
                map.put("y", row[0]);
                dataPoints.add(map);
            }
        }

        List<List<Map<Object, Object>>> result = new ArrayList<>();
        result.add(dataPoints);

        return result;
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<List<Map<Object, Object>>> getStatisticsCountRevenueByDateRange(String type, LocalDate startDate, LocalDate endDate) {
        List<Object[]> data;
        switch (type.toLowerCase()) {
            case "day":
                data = ticketRepository.countRevenueByDay(startDate, endDate);
                break;
            case "month":
                data = ticketRepository.countRevenueByMonth(startDate, endDate);
                break;
            case "year":
                data = ticketRepository.countRevenueByYear(startDate, endDate);
                break;
            case "route":
                data = ticketRepository.countRevenueByRoute(startDate, endDate);
                break;
            case "vehicle_type":
                data = ticketRepository.countRevenueByVehicleType(startDate, endDate);
                break;
            default:
                throw new IllegalArgumentException("Invalid type for statistics: " + type);
        }

        List<Map<Object, Object>> dataPoints = new ArrayList<>();
        if (type.equals("month")) {
            // Convert data into the required static structure
            for (Object[] row : data) {
                Map<Object, Object> map = new HashMap<>();
                map.put("x",  row[1].toString() +"/"+ row[0].toString());
                map.put("y", row[2]);
                dataPoints.add(map);
            }
        } else {
            // Convert data into the required static structure
            for (Object[] row : data) {
                Map<Object, Object> map = new HashMap<>();
                map.put("x", row[0]);
                map.put("y", row[1]);
                dataPoints.add(map);
            }
        }

        List<List<Map<Object, Object>>> result = new ArrayList<>();
        result.add(dataPoints);

        return result;
    }

}
