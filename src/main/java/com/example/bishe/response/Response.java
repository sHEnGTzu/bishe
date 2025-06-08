package com.example.bishe.response;

import com.example.bishe.entity.Paper;
import lombok.Data;

import java.util.List;

@Data
public class Response {
    String message;
    List<Paper> paperList;


    public Response(String message){
        setMessage(message);
    }

    public Response(String message,List<Paper> paperList){
        setPaperList(paperList);
        setMessage(message);
    }

}
