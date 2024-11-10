package com.tikjuti.bus_ticket_booking.configuration;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static String extractAfterLastHyphen(String input) {
        int lastHyphenIndex = input.lastIndexOf('-');
        if (lastHyphenIndex != -1) {
            return input.substring(lastHyphenIndex + 1); // Lấy chuỗi sau dấu "-"
        } else {
            return input; // Nếu không tìm thấy dấu "-", trả lại chuỗi gốc
        }
    }

    // Mã hóa chuỗi thành số (Dùng SHA-256 để đảm bảo độ dài cố định)
    public static String generateNumericHash(String input) {
        try {
            // Sử dụng SHA-256 để tạo hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // Chuyển đổi byte thành số
            BigInteger no = new BigInteger(1, hashBytes);

            // Chuyển đổi thành chuỗi số
            String result = no.toString(10); // Chuyển đổi thành chuỗi số thập phân

            // Lấy chuỗi số có độ dài mong muốn (Ví dụ: 10 ký tự)
            return result.length() > 10 ? result.substring(0, 20) : result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
