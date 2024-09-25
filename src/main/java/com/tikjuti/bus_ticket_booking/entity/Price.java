package com.tikjuti.bus_ticket_booking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    int ticketPrice;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    VehicleType vehicleType;

    @ManyToOne
    @JoinColumn(name = "route_id")
    Route route;
}

