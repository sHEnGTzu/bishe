package com.example.bishe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;

@Data
@Builder
@NoArgsConstructor // 添加无参构造函数
@AllArgsConstructor // 添加全参构造函数
public class Paper {
    int id;
    String user_name;
    String paper_name;
    String author;
    String subject;
    String keywords;
    String DOI;
    String Journal;

    public Paper(String user_name, String paper_name,String author,String subject,String keywords,String DOI,String Journal){
        this.user_name = user_name;
        this.paper_name = paper_name;
        this.author =  author;
        this.subject = subject;
        this.keywords = keywords;
        this.DOI = DOI;
        this.Journal = Journal;
    }
}
