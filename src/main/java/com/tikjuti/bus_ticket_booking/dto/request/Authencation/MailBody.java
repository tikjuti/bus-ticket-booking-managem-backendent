package com.tikjuti.bus_ticket_booking.dto.request.Authencation;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
