package com.example.bishe.SqlMapper;

import com.example.bishe.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface usersMapper {

    /*
    * 根据名字查找用户
    * */
    User findByName(String name);


    /*
    * 添加用户
    * */
    int addUser(User user);

}
