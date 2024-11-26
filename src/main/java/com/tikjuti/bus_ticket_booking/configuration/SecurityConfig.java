package com.tikjuti.bus_ticket_booking.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomJwtDecoder jwtDecoder;

    private final String[] PUBLIC_ENDPOINTS_POST = {
            "/api/tickets/buyTicket",
            "/api/tickets/lookup",
            "/api/seats/lock",
            "/api/customers",
            "/api/auth/token",
            "/api/auth/introspect",
            "/api/auth/logout",
            "/api/tickets",
            "/api/password/verifyEmail/{email}",
            "/api/password/verifyOtp/{otp}/{email}",
            "/api/password/changePassword/{email}",
            "/api/momoPayment/create",
            "/api/momoPayment/query",
            "/api/zaloPayment/create",
            "/api/zaloPayment/callback",
            "/api/zaloPayment/query",
            "/api/vnpay/query",
            "/api/vnpay/refund",
    };

    private final String[] PUBLIC_ENDPOINTS_GET = {
            "/api/tickets/{id}",
            "/api/vehicles/{id}",
            "/api/paymentMethods",
            "/api/paymentMethods/{id}",
            "/api/prices",
            "/api/routes",
            "/api/vnpay/create",
            "/api/tickets/buyTicket/{tripId}",
            "api/seats/{vehicleId}",
    };

    private final String[] PUBLIC_ENDPOINTS_PUT = {
            "/api/customers/{id}",
    };

    private final String[] PUBLIC_ENDPOINTS_PATCH = {
            "/api/customers/{id}",
            "/api/seats/{id}",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of("*"));
            corsConfiguration.setAllowedHeaders(List.of("*"));
            corsConfiguration.setAllowedMethods(List.of("*"));
            return corsConfiguration;
                }))
                .authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS_POST).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_GET).permitAll()
                        .requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINTS_PUT).permitAll()
                        .requestMatchers(HttpMethod.PATCH, PUBLIC_ENDPOINTS_PATCH).permitAll()

                        .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 ->
            oauth2.jwt(jwtConfigurer ->
                    jwtConfigurer.decoder(jwtDecoder)
                            .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
