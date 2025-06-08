package com.example.bishe.service;


import com.example.bishe.response.Response;

public interface PaperService {

    //根据用户名获取文献列表，以及所有用户都能查看的文献一同返回
    Response find_paper_by_username(String username);

    //添加文献
    Response add_paper(String username,String papername,String author,String subject,String keywords,String DOI,String Journal);

}
