package com.tikjuti.bus_ticket_booking.dto.request.Authencation;

public record ChangePassword(String password, String confirmPassword, Integer otp) {
}
