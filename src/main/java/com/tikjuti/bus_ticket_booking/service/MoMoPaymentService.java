package com.tikjuti.bus_ticket_booking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.shaded.gson.Gson;
import com.tikjuti.bus_ticket_booking.dto.request.MoMo.MoMoPaymentRequest;
import com.tikjuti.bus_ticket_booking.dto.request.MoMo.MoMoQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.ZaloPay.ZaloPayPaymentRequest;
import com.tikjuti.bus_ticket_booking.dto.response.MoMoPaymentResponse;
import com.tikjuti.bus_ticket_booking.dto.response.MoMoQueryResponse;
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
import java.util.*;


@Slf4j
@Service
public class MoMoPaymentService {
    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.partnerName}")
    private String partnerName;
    @Value("${momo.storeId}")
    private String storeId;
    @Value("${momo.lang}")
    private String lang;
    @Value("${momo.currency}")
    private String currency;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;
    @Value("${momo.endpoint_query}")
    private String endpoint_query;
    @Value("${momo.redirectUrl}")
    private String redirectUrl;
    @Value("${momo.ipnUrl}")
    private String ipnUrl;

    String requestType = "payWithMethod";
    Boolean autoCapture = true;
    String requestId = UUID.randomUUID().toString();

    public MoMoPaymentResponse createPayment(MoMoPaymentRequest request) throws Exception {
        String signature = generateSignature(request);

        Map<String, Object> userInfor = new HashMap<>();
        userInfor.put("name", request.getCustomerName());
        userInfor.put("phoneNumber", request.getPhone());
        userInfor.put("email", request.getEmail());

        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("id", request.getOrderId());
        item.put("name", request.getRouteName());
        item.put("description", request.getOrderInfo());
        item.put("price", request.getAmount());
        item.put("currency", currency);
        item.put("quantity", 1);
        item.put("totalPrice", request.getAmount());

        items.add(item);

        Map<String, Object> orderData = new HashMap<>() {
            {
                put("requestId", requestId);
                put("orderId", request.getOrderId());
                put("orderInfo", request.getOrderInfo());
                put("partnerCode", partnerCode);
                put("partnerName", partnerName);
                put("storeId", storeId);
                put("accessKey", accessKey);
                put("redirectUrl", redirectUrl);
                put("ipnUrl", ipnUrl);
                put("items", items);
                put("lang", lang);
                put("userInfor", userInfor);
                put("extraData", request.getExtraData());
                put("requestType", requestType);
                put("autoCapture", autoCapture);
                put("signature", signature);
                put("amount", request.getAmount());
            }
        };


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(orderData);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(endpoint);
            httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON)); // Chỉnh sửa ContentType
            httpPost.setHeader("Content-type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();

                if (statusCode == 200) {
                    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                    log.warn("Response from MoMo API: {}", responseString);

                    MoMoPaymentResponse momoResponse = objectMapper.readValue(responseString, MoMoPaymentResponse.class);

                    return momoResponse;
                } else {
                    throw new Exception("Error occurred while calling MoMo API. HTTP Status: " + statusCode);
                }
            }
        }
    }

    public MoMoQueryResponse query(MoMoQueryRequest request) throws Exception {

        String signature = generateQuerySignature(request);

        Map<String, Object> queryData = new HashMap<>() {
            {
                put("partnerCode", partnerCode);
                put("requestId", request.getRequestId());
                put("orderId", request.getOrderId());
                put("lang", lang);
                put("signature", signature);
            }
        };

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(queryData);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(endpoint_query);
            httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
            httpPost.setHeader("Content-type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();

                if (statusCode == 200) {
                    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                    log.warn("Response q from MoMo API: {}", responseString);

                    MoMoQueryResponse momoResponse = objectMapper.readValue(responseString, MoMoQueryResponse.class);

                    return momoResponse;
                } else {
                    throw new RuntimeException("Error occurred while calling MoMo Query API. HTTP Status: " + statusCode);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while calling MoMo Query API", e);
        }
    }

    private String generateQuerySignature(MoMoQueryRequest request) {
        String rawData = "accessKey="+ accessKey + "&orderId=" + request.getOrderId() +
                "&partnerCode=" + partnerCode + "&requestId=" + request.getRequestId();

        return HmacSHA256(secretKey, rawData);
    }

    private String generateSignature(MoMoPaymentRequest request) {
        String rawData = "accessKey=" + accessKey + "&amount=" + request.getAmount() +
                "&extraData=" + request.getExtraData() + "&ipnUrl=" + ipnUrl +
                "&orderId=" + request.getOrderId() + "&orderInfo=" + request.getOrderInfo() +
                "&partnerCode=" + partnerCode + "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId + "&requestType=" + requestType;

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
