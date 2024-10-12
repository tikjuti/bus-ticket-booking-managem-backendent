package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Customer findByEmailOrPhone(String email, String phone);

    boolean existsById(String id);
}
