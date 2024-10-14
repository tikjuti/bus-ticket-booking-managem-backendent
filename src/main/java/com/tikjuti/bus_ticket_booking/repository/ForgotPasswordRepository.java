package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, String> {
    boolean existsById(String id);

}
