package com.example.bishe.service;

import com.example.bishe.SqlMapper.papersMapper;
import com.example.bishe.entity.Paper;
import com.example.bishe.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperServiceImpl implements PaperService{

    @Autowired
    papersMapper papersMapper;

    @Override
    public Response find_paper_by_username(String username) {
        List<Paper> paperList = papersMapper.findByUsername(username);
        List<Paper> allpaperList = papersMapper.findByUsername("*");

        // 合并两个列表
        paperList.addAll(allpaperList);

        // 返回合并后的列表
        return new Response("获取成功", paperList);
    }

    @Override
    public Response add_paper(String username, String papername, String author, String subject, String keywords, String DOI, String Journal) {
        papersMapper.addPaper(new Paper(username,papername,author,subject,keywords,DOI,Journal));
        return new Response("添加成功");
    }

}
