package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.entity.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, String>,
    CustomForgotPasswordRepository{
    boolean existsById(String id);

    Optional<ForgotPassword> findByOtpAndAccount(Integer otp, Account account);

    Optional<ForgotPassword> findByAccount(Account account);
}
