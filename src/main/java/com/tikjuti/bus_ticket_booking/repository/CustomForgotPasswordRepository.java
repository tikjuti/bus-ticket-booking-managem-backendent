package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.entity.ForgotPassword;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomForgotPasswordRepository {
    Optional<ForgotPassword> findByOtpAndAccount(Integer otp, Account account);
}
