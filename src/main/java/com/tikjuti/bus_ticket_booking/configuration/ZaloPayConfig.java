package com.tikjuti.bus_ticket_booking.configuration;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZaloPayConfig {
    @Value("${zalopay.app_id}")
    int appId;

    @Value("${zalopay.key1}")
    String key1;

    @Value("${zalopay.key2}")
    String key2;

    @Value("${zalopay.endpoint}")
    String endpoint;

    @Value("${zalopay.callback_url}")
    String callbackUrl;

    @Value("${zalopay.redirect_base_url}")
    String redirectBaseUrl;

    @Value("${zalopay.endpoint_query}")
    String endpointQuery;
}
