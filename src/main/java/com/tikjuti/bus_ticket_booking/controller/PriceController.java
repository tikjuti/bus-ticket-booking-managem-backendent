package com.tikjuti.bus_ticket_booking.controller;

import com.tikjuti.bus_ticket_booking.dto.request.Price.PriceCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Price.PriceUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import com.tikjuti.bus_ticket_booking.dto.response.PriceResponse;
import com.tikjuti.bus_ticket_booking.dto.response.RouteResponse;
import com.tikjuti.bus_ticket_booking.entity.Price;
import com.tikjuti.bus_ticket_booking.entity.Route;
import com.tikjuti.bus_ticket_booking.service.PriceService;
import com.tikjuti.bus_ticket_booking.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {
    @Autowired
    private PriceService priceService;

    @PostMapping
    ResponseEntity<ApiResponse<Price>> createPrice(@RequestBody @Valid PriceCreationRequest request) {
        ApiResponse<Price> apiResponse = new ApiResponse<>();

        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Price created successfully");
        apiResponse.setResult(priceService.createPrice(request));

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<Price>>> getPrices()
    {
        List<Price> priceList = priceService.getPrices();

        ApiResponse<List<Price>> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Prices retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(priceList);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{priceId}")
    ResponseEntity<ApiResponse<PriceResponse>> getPrice(@PathVariable String priceId) {

        ApiResponse<PriceResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Price retrieved successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(priceService.getPrice(priceId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{priceId}")
    ResponseEntity<ApiResponse<PriceResponse>> updatePrice(@PathVariable String priceId,
                                                           @RequestBody PriceUpdateRequest request)
    {
        ApiResponse<PriceResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Price update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(priceService.updatePrice(request, priceId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{priceId}")
    ResponseEntity<ApiResponse<PriceResponse>> updatePatchPrice(@PathVariable String priceId,
                                                                @RequestBody PriceUpdateRequest request)
    {
        ApiResponse<PriceResponse> apiResponse = new ApiResponse<>();

        apiResponse.setMessage("Price update successfully");
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(priceService.patchUpdatePrice(request, priceId));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{priceId}")
    ResponseEntity<Void> deletePrice(@PathVariable String priceId) {

        priceService.deletePrice(priceId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
