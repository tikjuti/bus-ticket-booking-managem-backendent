package com.tikjuti.bus_ticket_booking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String fpId;

    @Column(nullable = false)
    Integer otp;

    @Column(nullable = false)
    Date expirationTime;

    @OneToOne
    Account account;

}
