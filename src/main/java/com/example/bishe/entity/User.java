package com.example.bishe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 添加无参构造函数
@AllArgsConstructor // 添加全参构造函数
public class User {
    int id;

    String name;

    String password;

    String email;

    public User(String name,String password,String email){
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
