package com.tikjuti.bus_ticket_booking.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.AuthenticationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.IntrospectRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.LogoutRequest;
import com.tikjuti.bus_ticket_booking.dto.response.AuthenticationResponse;
import com.tikjuti.bus_ticket_booking.dto.response.IntrospectResponse;
import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.entity.Customer;
import com.tikjuti.bus_ticket_booking.entity.InvalidatedToken;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.repository.AccountRepository;
import com.tikjuti.bus_ticket_booking.repository.CustomerRepository;
import com.tikjuti.bus_ticket_booking.repository.InvalidatedTokenRepository;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class AuthenticationService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoEndpoint;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenEndpoint;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String authorizationEndpoint;
    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.provider.facebook.authorization-uri}")
    private String authorizationEndpointFacebook;
    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    private String clientIdFacebook;
    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
    private String redirectUriFacebook;
    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    private String clientSecretFacebook;
    @Value("${spring.security.oauth2.client.provider.facebook.token-uri}")
    private String tokenEndpointFacebook;
    @Value("${spring.security.oauth2.client.registration.facebook.scope}")
    private String scopeFacebook;
    @Value("${spring.security.oauth2.client.provider.facebook.user-info-uri}")
    private String userInfoEndpointFacebook;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNING_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                        .valid(isValid)
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

        var token = generateToken(account);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public String socialLogin(String loginType) throws UnsupportedEncodingException {

        switch (loginType.trim().toLowerCase()) {
            case "google":
                return googleLogin();
            case "facebook":
                return facebookLogin();
            default:
                throw new IllegalArgumentException("Unsupported login type: " + loginType);
        }

    }

    private String formatScopes(String inputScopes) {
        return inputScopes.replaceAll(",", "").trim();
    }


    private String googleLogin()  {
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        String encodedScope = URLEncoder.encode(formatScopes(scope), StandardCharsets.UTF_8);

        String googleAuthUrl = authorizationEndpoint +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + encodedRedirectUri +
                "&scope=" + encodedScope;
        return googleAuthUrl;
    }

    private String facebookLogin() {
        String encodedRedirectUri = URLEncoder.encode(redirectUriFacebook, StandardCharsets.UTF_8);
        String encodedScope = URLEncoder.encode(formatScopes(scopeFacebook), StandardCharsets.UTF_8);

        String faceAuthUrl = authorizationEndpointFacebook +
                "?response_type=code" +
                "&client_id=" + clientIdFacebook +
                "&redirect_uri=" + encodedRedirectUri +
                "&scope=" + encodedScope;
        return faceAuthUrl;
    }

    public AuthenticationResponse handleGoogleCallback(String code, String loginType) throws JOSEException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        String accessToken;
        Gson gson = new Gson();

        switch (loginType.trim().toLowerCase()) {
            case "google":
                // Tạo request body dạng MultiValueMap
                MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
                tokenRequest.add("code", code);
                tokenRequest.add("client_id", clientId);
                tokenRequest.add("client_secret", clientSecret);
                tokenRequest.add("redirect_uri", redirectUri);
                tokenRequest.add("grant_type", "authorization_code");

                // Tạo headers cho request
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(tokenRequest, headers);

                // Gửi yêu cầu lấy access token
                ResponseEntity<String> response;
                try {
                    response = restTemplate.postForEntity(tokenEndpoint, request, String.class);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to fetch access token: " + e.getMessage(), e);
                }

                // Parse access token từ phản hồi
                Map<String, Object> tokenData = gson.fromJson(response.getBody(), Map.class);
                if (tokenData == null || !tokenData.containsKey("access_token")) {
                    throw new RuntimeException("Invalid token response: " + response.getBody());
                }
                accessToken = (String) tokenData.get("access_token");

                // Tạo headers cho yêu cầu lấy thông tin user
                HttpHeaders userInfoHeaders = new HttpHeaders();
                userInfoHeaders.setBearerAuth(accessToken);
                HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);

                // Gửi yêu cầu lấy thông tin user
                ResponseEntity<String> userInfoResponse;
                try {
                    userInfoResponse = restTemplate.exchange(
                            userInfoEndpoint,
                            HttpMethod.GET,
                            userInfoRequest,
                            String.class
                    );
                } catch (Exception e) {
                    throw new RuntimeException("Failed to fetch user info: " + e.getMessage(), e);
                }

                // Parse thông tin user từ phản hồi
                Map<String, Object> userInfo = gson.fromJson(userInfoResponse.getBody(), Map.class);
                if (userInfo == null || !userInfo.containsKey("email")) {
                    throw new RuntimeException("Invalid user info response: " + userInfoResponse.getBody());
                }

                String email = (String) userInfo.get("email");
                String name = (String) userInfo.get("name");

                Customer customer = customerRepository.findByEmailIgnoreCase(email);

                String token;
                Account account = null;
                if (customer == null) {
                    account = new Account();
                    account.setUsername(email);
                    account.setRoles(Set.of("GUEST"));
                    token = generateToken(account);
                    accountRepository.save(account);

                    Customer newCustomer = new Customer();
                    newCustomer.setAccount(account);
                    newCustomer.setEmail(email);
                    newCustomer.setCustomerName(name);
                    customerRepository.save(newCustomer);
                } else {
                    customer.setEmail(email);
                    customer.setCustomerName(name);
                    token = generateToken(customer.getAccount());
                }

                return AuthenticationResponse.builder()
                        .token(token)
                        .authenticated(true)
                        .build();
            default:
                throw new IllegalArgumentException("Unsupported login type: " + loginType);
        }
    }

    public AuthenticationResponse handleFacebookCallback(String code, String loginType) throws JOSEException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        String accessToken;
        Gson gson = new Gson();

        switch (loginType.trim().toLowerCase()) {
            case "facebook":
                // Tạo request body dạng MultiValueMap
                MultiValueMap<String, String> tokenRequestF = new LinkedMultiValueMap<>();
                tokenRequestF.add("code", code);
                tokenRequestF.add("client_id", clientIdFacebook);
                tokenRequestF.add("client_secret", clientSecretFacebook);
                tokenRequestF.add("redirect_uri", redirectUriFacebook);
                tokenRequestF.add("grant_type", "authorization_code");

                // Tạo headers cho request
                HttpHeaders headersF = new HttpHeaders();
                headersF.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, String>> requestF = new HttpEntity<>(tokenRequestF, headersF);

                // Gửi yêu cầu lấy access token
                ResponseEntity<String> responseF;
                try {
                    responseF = restTemplate.postForEntity(tokenEndpointFacebook, requestF, String.class);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to fetch access token: " + e.getMessage(), e);
                }

                // Parse access token từ phản hồi
                Map<String, Object> tokenDataF = gson.fromJson(responseF.getBody(), Map.class);
                if (tokenDataF == null || !tokenDataF.containsKey("access_token")) {
                    throw new RuntimeException("Invalid token response: " + responseF.getBody());
                }
                accessToken = (String) tokenDataF.get("access_token");

                // Tạo headers cho yêu cầu lấy thông tin user
                HttpHeaders userInfoHeadersF = new HttpHeaders();
                userInfoHeadersF.setBearerAuth(accessToken);
                HttpEntity<Void> userInfoRequestF = new HttpEntity<>(userInfoHeadersF);

                // Gửi yêu cầu lấy thông tin user
                ResponseEntity<String> userInfoResponseF;
                try {
                    userInfoResponseF = restTemplate.exchange(
                            userInfoEndpointFacebook,
                            HttpMethod.GET,
                            userInfoRequestF,
                            String.class
                    );
                } catch (Exception e) {
                    throw new RuntimeException("Failed to fetch user info: " + e.getMessage(), e);
                }

                // Parse thông tin user từ phản hồi
                Map<String, Object> userInfo = gson.fromJson(userInfoResponseF.getBody(), Map.class);
                if (userInfo == null || !userInfo.containsKey("email")) {
                    throw new RuntimeException("Invalid user info response: " + userInfoResponseF.getBody());
                }

                String emailF = (String) userInfo.get("email");
                String nameF = (String) userInfo.get("name");

                Customer customerF = customerRepository.findByEmailIgnoreCase(emailF);

                String tokenF;
                Account accountF = null;
                if (customerF == null) {
                    accountF = new Account();
                    accountF.setUsername(emailF);
                    accountF.setRoles(Set.of("GUEST"));
                    tokenF = generateToken(accountF);
                    accountRepository.save(accountF);

                    Customer newCustomer = new Customer();
                    newCustomer.setAccount(accountF);
                    newCustomer.setEmail(emailF);
                    newCustomer.setCustomerName(nameF);
                    customerRepository.save(newCustomer);
                } else {
                    customerF.setEmail(emailF);
                    customerF.setCustomerName(nameF);
                    tokenF = generateToken(customerF.getAccount());
                }


                return AuthenticationResponse.builder()
                        .token(tokenF)
                        .authenticated(true)
                        .build();
            default:
                throw new IllegalArgumentException("Unsupported login type: " + loginType);
        }
    }


    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signedToken = verifyToken(request.getToken());

        String jit = signedToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNING_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified || expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String generateToken(Account account) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("bus-ticket.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, java.time.temporal.ChronoUnit.DAYS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(account))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(SIGNING_KEY.getBytes()));

        return jwsObject.serialize();
    }

    private String buildScope(Account account) {
        StringJoiner joiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(account.getRoles())) {
            account.getRoles().forEach(joiner::add);
        }

        return joiner.toString();
    }
}
