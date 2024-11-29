
package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/totalTickets")
    public ResponseEntity<List<List<Map<Object, Object>>>> getTicketStatistics(
            @RequestParam String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<List<Map<Object, Object>>> statistics = statisticsService.getStatisticsByDateRange(type, startDate, endDate);
        return ResponseEntity.ok(statistics);
    }
    @GetMapping("/countRevenue")
    public ResponseEntity<List<List<Map<Object, Object>>>> getTicketStatisticCountRevenue(
            @RequestParam String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<List<Map<Object, Object>>> statistics = statisticsService.getStatisticsCountRevenueByDateRange(type, startDate, endDate);
        return ResponseEntity.ok(statistics);
    }
}
