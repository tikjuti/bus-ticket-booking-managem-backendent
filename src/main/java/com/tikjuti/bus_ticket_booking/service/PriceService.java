package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Price.PriceCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Price.PriceUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.PriceResponse;
import com.tikjuti.bus_ticket_booking.entity.Price;
import com.tikjuti.bus_ticket_booking.entity.Route;
import com.tikjuti.bus_ticket_booking.entity.VehicleType;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.PriceMapper;
import com.tikjuti.bus_ticket_booking.mapper.RouteMapper;
import com.tikjuti.bus_ticket_booking.mapper.VehicleTypeMapper;
import com.tikjuti.bus_ticket_booking.repository.PriceRepository;
import com.tikjuti.bus_ticket_booking.repository.RouteRepository;
import com.tikjuti.bus_ticket_booking.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {
    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private PriceMapper priceMapper;
    @Autowired
    private VehicleTypeMapper vehicleTypeMapper;
    @Autowired
    private RouteMapper routeMapper;

    public Price createPrice(PriceCreationRequest request)
    {
        Price price = new Price();

        Boolean exitsPrice = priceRepository.checkPrice(
                request.getVehicleTypeId(),
                request.getRouteId(),
                price.getId()
        );
        if(exitsPrice)
            throw new AppException(ErrorCode.PRICE_EXISTED);

        VehicleType vehicleType = vehicleTypeRepository
                .findById(request.getVehicleTypeId())
                .orElseThrow(() -> new RuntimeException("Vehicle type not found"));

        Route route = routeRepository
                .findById(request.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));

        price.setTicketPrice(request.getTicketPrice());
        price.setVehicleType(vehicleType);
        price.setRoute(route);

        return priceRepository
                .save(price);
    }

    public List<Price> getPrices() {return priceRepository.findAll();}

    public PriceResponse getPrice(String priceId)
    {
        return priceMapper
                .toPriceResponse(priceRepository
                .findById(priceId)
                .orElseThrow(() -> new RuntimeException("Price not found")));
    }

    public PriceResponse updatePrice(PriceUpdateRequest request, String priceId)
    {
        Price price = priceRepository
                .findById(priceId)
                .orElseThrow(() -> new RuntimeException("Price not found"));

        Boolean exitsPrice = priceRepository.checkPrice(
                request.getVehicleTypeId(),
                request.getRouteId(),
                priceId
        );

        if(exitsPrice)
            throw new AppException(ErrorCode.PRICE_EXISTED);

        VehicleType vehicleType = vehicleTypeRepository
                .findById(request.getVehicleTypeId())
                .orElseThrow(() -> new RuntimeException("Vehicle type not found"));

        Route route = routeRepository
                .findById(request.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));

        price.setTicketPrice(request.getTicketPrice());
        price.setVehicleType(vehicleType);
        price.setRoute(route);

        return priceMapper
                .toPriceResponse(priceRepository.save(price));
    }

    public PriceResponse patchUpdatePrice(PriceUpdateRequest request, String priceId) {
        Price price = priceRepository
                .findById(priceId)
                .orElseThrow(() -> new RuntimeException("Price not found"));

        Boolean existsPrice = priceRepository.checkPrice(
                request.getVehicleTypeId() != null ? request.getVehicleTypeId() : price.getVehicleType().getId(),
                request.getRouteId() != null ? request.getRouteId() : price.getRoute().getId(),
                priceId
        );

        if(existsPrice)
            throw new AppException(ErrorCode.PRICE_EXISTED);

        if (request.getVehicleTypeId() != null) {
            VehicleType vehicleType = vehicleTypeRepository
                    .findById(request.getVehicleTypeId())
                    .orElseThrow(() -> new RuntimeException("Vehicle type not found"));
            price.setVehicleType(vehicleType);
        }

        if (request.getRouteId() != null) {
            Route route = routeRepository
                    .findById(request.getRouteId())
                    .orElseThrow(() -> new RuntimeException("Route not found"));
            price.setRoute(route);
        }

        if (request.getTicketPrice() != 0 && request.getTicketPrice() > 0) {
            price.setTicketPrice(request.getTicketPrice());
        }

        return priceMapper.toPriceResponse(priceRepository.save(price));
    }


    public void deletePrice(String priceId) {
        priceRepository.findById(priceId)
                .map(price -> {
                    priceRepository.delete(price);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Price not found for ID: " + priceId));
    }
}
