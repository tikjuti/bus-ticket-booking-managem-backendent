package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
    boolean existsByMethodName(String methodName);

    boolean existsById(String id);
}
