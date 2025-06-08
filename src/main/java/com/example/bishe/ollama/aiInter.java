package com.example.bishe.ollama;

import java.io.IOException;
import java.util.concurrent.*;
import okhttp3.*;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.client.RestClient;

import static com.example.bishe.ollama.DeepSeekChatRemote.makeApiCall;

public class aiInter {
    // 手动创建 OllamaChatClient 实例,不依赖自动注入
    OllamaChatClient ollamaChatClient;


    public aiInter() {
        //本地模型
        String ollamaBaseUrl = "http://localhost:11434";

        OllamaApi ollamaApi = new OllamaApi(ollamaBaseUrl);
        this.ollamaChatClient = new OllamaChatClient(ollamaApi);
    }



    public String tiwen(String msg, float temperature, String model) {
        int retryCount = 0;
        while (true) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(() -> {
                System.out.println("提问中...");
                Prompt prompt = new Prompt(
                        msg,
                        OllamaOptions.create()
                                .withModel(model)
                                .withTemperature(temperature));
                ChatResponse chatResponse = ollamaChatClient.call(prompt);
                System.out.println("模型回答完毕");
                return chatResponse.getResult().getOutput().getContent();
            });

            try {
                return future.get(120, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                System.out.println("提问超时，第 " + (retryCount + 1) + " 次重试...");
                future.cancel(true); // 取消任务
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("提问过程中出现异常: " + e.getMessage());
                break;
            } finally {
                executor.shutdownNow(); // 关闭线程池
            }
            retryCount++;
        }
        return null;
    }

    public String tiwen(String msg){
        String apiKey = "sk-8a85efbf334346acadaa906a86f28af8";
        String baseUrl = "https://api.deepseek.com/v1/chat/completions";

        try {
            return makeApiCall(apiKey, baseUrl,msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
