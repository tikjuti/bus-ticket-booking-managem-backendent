package com.tikjuti.bus_ticket_booking.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.AuthenticationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.IntrospectRequest;
import com.tikjuti.bus_ticket_booking.dto.response.AuthenticationResponse;
import com.tikjuti.bus_ticket_booking.dto.response.IntrospectResponse;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.repository.AccountRepository;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Service
public class AuthenticationService {
    @Autowired
    private AccountRepository accountRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNING_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNING_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                        .valid(verified && expirationTime.after(new Date()))
                        .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException {
        var account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(account.getUsername());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(String username) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("bus-ticket.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, java.time.temporal.ChronoUnit.DAYS).toEpochMilli()
                ))
                .claim("customName", "customValue")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(SIGNING_KEY.getBytes()));

        return jwsObject.serialize();
    }
}
