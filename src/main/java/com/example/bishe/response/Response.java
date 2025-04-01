package com.example.bishe.response;

import lombok.Data;

@Data
public class Response {
    String message;


    public Response(String message){
        setMessage(message);
    }

}
