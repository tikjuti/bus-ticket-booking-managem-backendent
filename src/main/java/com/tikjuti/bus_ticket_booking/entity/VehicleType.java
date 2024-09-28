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
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String vehicleTypeName;

}
