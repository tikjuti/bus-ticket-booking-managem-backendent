
package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.dto.request.Ticket.TicketCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Ticket.TicketQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Ticket.TicketUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Ticket.BuyTicketRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.BuyTicketResponse;
import com.tikjuti.bus_ticket_booking.dto.response.TicketResponse;
import com.tikjuti.bus_ticket_booking.entity.Ticket;
import com.tikjuti.bus_ticket_booking.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/buyTicket")
    ResponseEntity<ApiResponse<List<BuyTicketResponse>>> getTripUserFind(@RequestBody BuyTicketRequest request) {

        ApiResponse<List<BuyTicketResponse>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Find ticket successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(ticketService.buyTicket(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/buyTicket/{tripId}")
    ResponseEntity<ApiResponse<BuyTicketResponse>> getTripUserFindById(@PathVariable String tripId) {

        ApiResponse<BuyTicketResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Find trip successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(ticketService.buyTicketById(tripId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<ApiResponse<Ticket>> createTicket(@RequestBody TicketCreationRequest request) {
        ApiResponse<Ticket> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Ticket created successfully");
        apiResponse.setResult(ticketService.createTicket(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<ApiResponse<PaginatedResult<Ticket>>> getTickets(TicketQueryRequest queryRequest) {
        PaginatedResult<Ticket> ticketList = ticketService.getTickets(queryRequest);

        ApiResponse<PaginatedResult<Ticket>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Tickets retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(ticketList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}")
    ResponseEntity<ApiResponse<TicketResponse>> getTicket(@PathVariable String ticketId) {

        ApiResponse<TicketResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Ticket retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(ticketService.getTicket(ticketId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{ticketId}")
    ResponseEntity<ApiResponse<TicketResponse>> updateTicket(@PathVariable String ticketId,
                                                             @RequestBody TicketUpdateRequest request)
    {
        ApiResponse<TicketResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Ticket update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(ticketService.updateTicket(request, ticketId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{ticketId}")
    ResponseEntity<ApiResponse<TicketResponse>> updatePatchTicket(@PathVariable String ticketId,
                                                                  @RequestBody TicketUpdateRequest request)
    {
        ApiResponse<TicketResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Ticket update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(ticketService.patchUpdateTicket(request, ticketId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{ticketId}")
    ResponseEntity<Void> deleteTicket(@PathVariable String ticketId) {

        ticketService.deleteTicket(ticketId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
