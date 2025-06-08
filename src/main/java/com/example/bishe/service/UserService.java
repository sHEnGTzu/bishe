package com.example.bishe.service;


import com.example.bishe.response.Response;

public interface UserService {

    //登录
    Response login(String username,String password);

    //注册
    Response register(String username,String password,String email);

}
