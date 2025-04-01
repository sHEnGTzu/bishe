package com.example.bishe.service;

import com.example.bishe.entity.entity;

public interface NodeService {



    //创建结点建立关系或者找到已有结点并建立关系
    void createRelationBetweenNodes(String nodeName1,String nodeName2,String relation,String title);

    //创建实体
    entity createEntity(String name,String title);

    //查找结点
    entity findNodeByName(String name);


    //创建关系
    void createRelation(entity entity1, entity entity2, String relationName,String title);


}
