package com.tikjuti.bus_ticket_booking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String departureLocation;
    String arrivalLocation;
    String departurePoint;
    String arrivalPoint;
    int distance;
    int duration;

    @OneToMany(mappedBy = "ticketPrice", cascade = CascadeType.ALL)
    Set<Price> prices = new HashSet<>();
}
