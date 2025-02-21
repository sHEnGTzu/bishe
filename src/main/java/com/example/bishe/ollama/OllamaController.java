//package com.example.bishe.ollama;
//
//import org.springframework.ai.chat.ChatResponse;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.ollama.OllamaChatClient;
//import org.springframework.ai.ollama.api.OllamaOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//
////启动失败先注释此程序，启动完关闭后再取消注释并启动
//@RestController
//public class OllamaController {           //依赖自动注入的ai提问接口
//
//    @Autowired
//    @Qualifier("ollamaChatClient")
//    private OllamaChatClient ollamaChatClient;
//
//    /**
//     * http://localhost:8080/ollama/chat/v1?msg=三角形的内角和是多少度？
//     */
//    @GetMapping("/ollama/chat/v1")
//    public String ollamaChat(@RequestParam String msg) {
//        return this.ollamaChatClient.call(msg);
//    }
//
//    /**
//     * http://localhost:8080/ollama/chat/v2?msg=人为什么要不断的追求卓越？
//     */
//    @GetMapping("/ollama/chat/v2")
//    public Object ollamaChatV2(@RequestParam String msg) {
//        Prompt prompt = new Prompt(msg);
//        ChatResponse chatResponse = ollamaChatClient.call(prompt);
//        return chatResponse;
//    }
//
//    /**
//     * http://localhost:8080/ollama/chat/v3?msg=三角形的内角和是多少度
//     */
//    @GetMapping("/ollama/chat/v3")
//    public Object ollamaChatV3(@RequestParam String msg) {
//        Prompt prompt = new Prompt(
//                msg,
////                OllamaOptions.create()
////                        .withModel("deepseek-r1:1.5b")
////                        .withTemperature(0.4F));
//                OllamaOptions.create()
//                        .withModel("deepseek-r1:1.5b")
//                        .withTemperature(0F));
//        ChatResponse chatResponse = ollamaChatClient.call(prompt);
//        return chatResponse.getResult().getOutput().getContent();
//    }
//
//
//}