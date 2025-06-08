package com.example.bishe.service;

import com.example.bishe.SqlMapper.usersMapper;
import com.example.bishe.entity.User;
import com.example.bishe.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    usersMapper usersMapper;


    @Override
    public Response login(String username, String password) {
        User user = usersMapper.findByName(username);
        if (user == null) return new Response("登录失败,账号不存在");
        Response response;
        if (user.getPassword().equals(password)) response = new Response("登录成功");
        else response = new Response("登录失败,密码或账号错误");
        return response;
    }

    @Override
    public Response register(String username, String password, String email) {
        User user = new User(username,password,email);
        usersMapper.addUser(user);
        return new Response("注册成功");
    }
}
