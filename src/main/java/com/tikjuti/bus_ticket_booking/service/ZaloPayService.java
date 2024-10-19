package com.tikjuti.bus_ticket_booking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.tikjuti.bus_ticket_booking.Utils.DateTimeUtil;
import com.tikjuti.bus_ticket_booking.Utils.HMACUtil;
import com.tikjuti.bus_ticket_booking.configuration.ZaloPayConfig;
import com.tikjuti.bus_ticket_booking.dto.request.ZaloPay.CallbackRequest;
import com.tikjuti.bus_ticket_booking.dto.request.ZaloPay.ZaloPayQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.ZaloPay.ZaloPayPaymentRequest;
import com.tikjuti.bus_ticket_booking.dto.response.ZaloPayPaymentResponse;
import com.tikjuti.bus_ticket_booking.dto.response.ZaloPayQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ZaloPayService {
    @Autowired
    private ZaloPayConfig zaloPayConfig;

    public ZaloPayPaymentResponse createPayment(ZaloPayPaymentRequest request) throws Exception {
        int app_id = zaloPayConfig.getAppId();
        String key1 = zaloPayConfig.getKey1();
        String endpoint = zaloPayConfig.getEndpoint();
        String callbackUrl = zaloPayConfig.getCallbackUrl();
        String redirectBaseUrl = zaloPayConfig.getRedirectBaseUrl();

        List<ZaloPayPaymentRequest> items = new ArrayList<>();
        items.add(new ZaloPayPaymentRequest(request.getOrderId(), request.getCustomerName(), request.getPhone(), request.getAmount()));

        Gson gson = new Gson();
        String itemsJson = gson.toJson(items);
        final Map embed_data = new HashMap() {

            {
                put("redirecturl", redirectBaseUrl + "/" + request.getOrderId());
            }
        };

        String embedDataJson = gson.toJson(embed_data);

        Map<String, Object> orderData = new HashMap<String, Object>();
        orderData.put("app_id", app_id);
        orderData.put("app_user", request.getCustomerName()+request.getPhone());
        orderData.put("app_time", System.currentTimeMillis());
        orderData.put("amount", request.getAmount());
        orderData.put("app_trans_id", DateTimeUtil.getCurrentTimeString("yyMMdd") + "_" + request.getOrderId());
        orderData.put("bank_code", "");
        orderData.put("embed_data", embedDataJson);
        orderData.put("item", itemsJson);
        orderData.put("callback_url", callbackUrl);
        orderData.put("description", "Thanh toán chuyến " + request.getOrderId());

        // Tạo signature
        String data = orderData.get("app_id") + "|" + orderData.get("app_trans_id") + "|" + orderData.get("app_user") + "|" + orderData.get("amount")
                + "|" + orderData.get("app_time") + "|" + orderData.get("embed_data") + "|" + orderData.get("item");
        orderData.put("mac", HMACUtil.HMacSHA256(data, key1));

        log.warn("orderData: {}", orderData);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(endpoint);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(orderData);

            StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
            postRequest.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                int statusCode = response.getCode();
                if (statusCode >= 200 && statusCode < 300) {
                    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

                    return objectMapper.readValue(responseString, ZaloPayPaymentResponse.class);
                } else {
                    throw new RuntimeException("Failed with HTTP error code : " + statusCode);
                }
            }
        }
    }

    public String callBack(CallbackRequest request) throws Exception {
        JsonObject jsonObject = new JsonObject();
        try {
            String key2 = zaloPayConfig.getKey2();
            String data = request.getData();
            String mac = request.getMac();

            String generatedMac = HMACUtil.HMacSHA256(data, key2);

            if (!mac.equals(generatedMac)) {
                jsonObject.addProperty("return_code", -1);
                jsonObject.addProperty("return_message", "Invalid MAC");
            } else {
                JsonObject jsonObjectData = new Gson().fromJson(data, JsonObject.class);
                String appTransId = jsonObjectData.get("app_trans_id").getAsString();

                log.warn("appTransId: {}", appTransId);
                jsonObject.addProperty("return_code", 1);
                jsonObject.addProperty("return_message", "Success");
            }

        } catch (Exception e) {
            jsonObject.addProperty("return_code", 0);
            jsonObject.addProperty("return_message", e.getMessage());
        }
        return jsonObject.toString();
    }

    public ZaloPayQueryResponse query(ZaloPayQueryRequest request) throws Exception {
        int appId = zaloPayConfig.getAppId();
        String appTransId = request.getApp_trans_id();
        String key1 = zaloPayConfig.getKey1();
        String endpointQuery = zaloPayConfig.getEndpointQuery();

        Map<String, Object> queryData = new HashMap<String, Object>();
        queryData.put("app_id", appId);
        queryData.put("app_trans_id", appTransId);

        String macData = appId + "|" + appTransId + "|" + key1;
        queryData.put("mac", HMACUtil.HMacSHA256(macData, key1));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(endpointQuery);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(queryData);

            StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
            postRequest.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                int statusCode = response.getCode();
                if (statusCode >= 200 && statusCode < 300) {
                    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

                    return objectMapper.readValue(responseString, ZaloPayQueryResponse.class);
                } else {
                    throw new RuntimeException("Failed with HTTP error code : " + statusCode);
                }
            }
        }
    }
}
