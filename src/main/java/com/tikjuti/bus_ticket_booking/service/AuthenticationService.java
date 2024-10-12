package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Account.AuthenticationRequest;
import com.tikjuti.bus_ticket_booking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private AccountRepository accountRepository;

    public boolean authenticate(AuthenticationRequest request) {
        var account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassword(), account.getPassword());
    }
}
