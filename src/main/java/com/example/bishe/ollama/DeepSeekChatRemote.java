package com.example.bishe.ollama;

import okhttp3.*;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DeepSeekChatRemote {
    // 构建请求体的方法
    private static Map<String, Object> buildRequestBody(String msg) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant");

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", msg);

        requestBody.put("messages", Arrays.asList(systemMessage, userMessage));
        requestBody.put("stream", false);

        return requestBody;
    }

    // 创建 OkHttp 请求的方法
    private static Request createRequest(String apiKey, String baseUrl, String msg) {
        Map<String, Object> requestBody = buildRequestBody(msg);
        Gson gson = new Gson();
        String json = gson.toJson(requestBody);

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        return new Request.Builder()
                .url(baseUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();
    }

    // 执行 API 调用的方法
    public static String makeApiCall(String apiKey, String baseUrl, String msg) throws IOException {
        // 设置超时时间为两分钟
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120000, java.util.concurrent.TimeUnit.MILLISECONDS)
                .readTimeout(120000, java.util.concurrent.TimeUnit.MILLISECONDS)
                .writeTimeout(120000, java.util.concurrent.TimeUnit.MILLISECONDS)
                .build();
        Request request = createRequest(apiKey, baseUrl, msg);

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    return parseResponse(responseBody);
                }
            } else {
                System.out.println("Request failed: " + response.message());
            }
        }
        return null;
    }

    // 解析响应 JSON 的方法
    private static String parseResponse(String responseBody) {
        Gson gson = new Gson();
        Map<?, ?> responseMap = gson.fromJson(responseBody, Map.class);
        Map<?, ?> choice = (Map<?, ?>) ((java.util.List<?>) responseMap.get("choices")).get(0);
        Map<?, ?> message = (Map<?, ?>) choice.get("message");
        return (String) message.get("content");
    }
}