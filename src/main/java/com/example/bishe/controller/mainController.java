package com.example.bishe.controller;

import com.example.bishe.response.Response;
import com.example.bishe.userQuestion.UserQuestion;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class mainController {

    UserQuestion userQuestion;

    @PostMapping("/question")
    public Response question(@RequestBody Map<String,String> map){
        userQuestion = new UserQuestion(map.get("message"),"deepseek-r1:7b");
        return new Response(userQuestion.main());
    }

}
