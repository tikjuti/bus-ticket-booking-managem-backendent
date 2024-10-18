package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.MoMo.MoMoPaymentRequest;
import com.tikjuti.bus_ticket_booking.dto.response.MoMoPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Hex;
import org.apache.hc.core5.http.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


@Slf4j
@Service
public class MoMoPaymentService {
    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;


    public MoMoPaymentResponse createPayment(MoMoPaymentRequest request) throws Exception {
        // Tạo chữ ký (signature)
        String signature = generateSignature(request);
        request.setSignature(signature);

        // Convert request object to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        // Gửi yêu cầu HTTP đến MoMo API
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(endpoint);
            httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON)); // Chỉnh sửa ContentType
            httpPost.setHeader("Content-type", "application/json");

            // Thực hiện yêu cầu và xử lý phản hồi
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();

                // Kiểm tra mã trạng thái HTTP
                if (statusCode == 200) {
                    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                    log.warn("Response from MoMo API: {}", responseString);

                    // Chuyển đổi JSON phản hồi từ MoMo thành đối tượng MoMoPaymentResponse
                    MoMoPaymentResponse momoResponse = objectMapper.readValue(responseString, MoMoPaymentResponse.class);

                    // Trả về đối tượng MoMoPaymentResponse
                    return momoResponse;
                } else {
                    // Nếu mã trạng thái không phải 200, ném ngoại lệ với thông tin chi tiết
                    throw new Exception("Error occurred while calling MoMo API. HTTP Status: " + statusCode);
                }
            }
        }
    }



    private String generateSignature(MoMoPaymentRequest request) {
        // Tạo chữ ký bằng cách kết hợp các thông tin request và mã hóa bằng HmacSHA256
        String rawData = "accessKey=" + accessKey + "&amount=" + request.getAmount() +
                "&extraData=" + request.getExtraData() + "&ipnUrl=" + request.getIpnUrl() +
                "&orderId=" + request.getOrderId() + "&orderInfo=" + request.getOrderInfo() +
                "&partnerCode=" + partnerCode + "&redirectUrl=" + request.getRedirectUrl() +
                "&requestId=" + request.getRequestId() + "&requestType=" + request.getRequestType();

        return HmacSHA256(secretKey, rawData);
    }

    private String HmacSHA256(String secretKey, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacData = mac.doFinal(data.getBytes());
            return Hex.encodeHexString(hmacData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC SHA-256", e);
        }
    }
}
