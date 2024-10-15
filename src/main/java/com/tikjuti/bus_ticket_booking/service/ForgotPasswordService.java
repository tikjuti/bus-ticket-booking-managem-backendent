package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Authencation.ChangePassword;
import com.tikjuti.bus_ticket_booking.dto.request.Authencation.MailBody;
import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.entity.Customer;
import com.tikjuti.bus_ticket_booking.entity.Employee;
import com.tikjuti.bus_ticket_booking.entity.ForgotPassword;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.repository.AccountRepository;
import com.tikjuti.bus_ticket_booking.repository.CustomerRepository;
import com.tikjuti.bus_ticket_booking.repository.EmployeeRepository;
import com.tikjuti.bus_ticket_booking.repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
public class ForgotPasswordService {

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void  verifyEmail(String email) {
        Account account = null;

        account = getAccountByEmail(email);

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your forgot password request: " + otp)
                .subject("Forgot Password OTP")
                .build();

        ForgotPassword forgotPassword = forgotPasswordRepository.findByAccount(account)
                .orElse(null);

        if (forgotPassword != null) {
            forgotPasswordRepository.deleteById(forgotPassword.getFpId());
        } else {
            forgotPassword = ForgotPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 180 * 1000))
                    .account(account)
                    .build();
        }

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(forgotPassword);

    }

    public void verifyOtp(String email, int otp) {
        Account account = null;

        account = getAccountByEmail(email);

        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndAccount(otp, account)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (forgotPassword.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(forgotPassword.getFpId());
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }
    }

    public void changePassword(String email, ChangePassword changePassword) {

        verifyOtp(email, changePassword.otp());

        if (!Objects.equals(changePassword.password(), changePassword.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());

        Account account = getAccountByEmail(email);
        account.setPassword(encodedPassword);

        accountRepository.save(account);
    }

    private Account getAccountByEmail(String email) {
        Account account = null;

        if (customerRepository.existsByEmail(email)) {
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            account = accountRepository.findById(customer.getAccount().getId())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
        } else if (employeeRepository.existsByEmail(email)) {
            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            account = accountRepository.findById(employee.getAccount().getId())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
        } else {
            throw new RuntimeException("Email not found in the system");
        }

        return account;
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
