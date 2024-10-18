package com.tikjuti.bus_ticket_booking.Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HMACUtil {
    public static String HMacSHA256(String data, String key) throws Exception {
        // Tạo đối tượng Mac với thuật toán HmacSHA256
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        // Chuyển khóa sang dạng bytes với UTF-8
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        // Khởi tạo đối tượng Mac với khóa
        sha256_HMAC.init(secret_key);

        // Mã hóa dữ liệu (input) và trả về kết quả dạng Base64
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0'); // Thêm '0' nếu độ dài là 1
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
