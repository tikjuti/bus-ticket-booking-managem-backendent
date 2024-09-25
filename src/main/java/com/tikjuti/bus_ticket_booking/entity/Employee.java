package com.tikjuti.bus_ticket_booking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String employeeName;
    String gender;
    String address;
    String phone;
    String email;
    LocalDateTime dob;
    String nationalIDNumber;
    String username;
    String password;
    String status;

    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "employee_type_id")
    EmployeeType employeeType;

}
