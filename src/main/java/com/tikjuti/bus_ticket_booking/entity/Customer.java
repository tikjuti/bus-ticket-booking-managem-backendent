package com.tikjuti.bus_ticket_booking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String customerName;
    String gender;
    String address;
    String phone;
    String email;
    LocalDateTime dob;

    @OneToOne
    @JoinColumn(name = "account_id")
    Account account;
}
