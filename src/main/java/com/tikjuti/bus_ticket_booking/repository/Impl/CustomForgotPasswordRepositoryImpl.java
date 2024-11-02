package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.entity.ForgotPassword;
import com.tikjuti.bus_ticket_booking.repository.CustomForgotPasswordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomForgotPasswordRepositoryImpl implements CustomForgotPasswordRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ForgotPassword> findByOtpAndAccount(Integer otp, Account account) {
        String jpql = "SELECT f FROM ForgotPassword f WHERE f.otp = :otp AND f.account = :account";
        return entityManager.createQuery(jpql, ForgotPassword.class)
                .setParameter("otp", otp)
                .setParameter("account", account)
                .getResultStream()
                .findFirst();
    }
}
