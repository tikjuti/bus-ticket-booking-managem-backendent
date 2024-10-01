package com.tikjuti.bus_ticket_booking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    int seatCount;
    String licensePlate;
    String vehicleName;
    String color;
    String status;

    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    VehicleType vehicleType;
}
