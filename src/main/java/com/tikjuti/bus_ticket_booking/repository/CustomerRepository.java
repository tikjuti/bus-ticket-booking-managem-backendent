package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Customer findByEmailOrPhone(String email, String phone);

    Optional<Customer> findByEmail(String email);
    Customer findByEmailIgnoreCase(String email);

    boolean existsById(String id);
}
