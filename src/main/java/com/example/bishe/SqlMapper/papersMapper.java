package com.example.bishe.SqlMapper;

import com.example.bishe.entity.Paper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface papersMapper {
    //添加文献
    int addPaper(Paper paper);

    //通过用户名查找
    List<Paper> findByUsername(String user_name);

    //删除文献
    int deletePaper(String paper_name);

}
