package com.example.bishe.ollama;


import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

public class aiInter {
    // 手动创建 OllamaChatClient 实例,不依赖自动注入
    private final OllamaChatClient ollamaChatClient;

    public aiInter() {
        // 假设 Ollama 服务运行在本地的默认端口 11434
        String ollamaBaseUrl = "http://localhost:11434";
        // 创建 OllamaApi 实例
        OllamaApi ollamaApi = new OllamaApi(ollamaBaseUrl);
        this.ollamaChatClient = new OllamaChatClient(ollamaApi);
    }

    public String tiwen(String msg,float temperature,String model){
        System.out.println("提问中...");
        Prompt prompt = new Prompt(
                msg,
                OllamaOptions.create()
                        .withModel(model)
                        .withTemperature(temperature));
        ChatResponse chatResponse = ollamaChatClient.call(prompt);
        System.out.println("模型回答完毕");
        return chatResponse.getResult().getOutput().getContent();
    }

}
