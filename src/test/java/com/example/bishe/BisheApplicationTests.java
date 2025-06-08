package com.example.bishe;

import com.example.bishe.SqlMapper.papersMapper;
import com.example.bishe.SqlMapper.usersMapper;
import com.example.bishe.entity.Paper;
import com.example.bishe.neo4jDAO.NodeRepository;
import com.example.bishe.service.NodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;



@SpringBootTest
class BisheApplicationTests {

    @Autowired
    NodeRepository NodeRepository;

    @Autowired
    NodeService nodeService;

    @Autowired
    usersMapper usersMapper;

    @Autowired
    papersMapper papersMapper;


    @Test
    public void testCreate() throws IOException {
        System.out.println(papersMapper.addPaper(new Paper("shengt","论文11","摘要10","1","1","1","1")));
        //提取实体关系，构建知识图谱
//        Entity_Relation_reco entity_Relation_reco = new Entity_Relation_reco("C:\\Users\\24204\\Desktop\\paper\\1.pdf","测试标题");
//        System.out.println(entity_Relation_reco.sentences.size());
//        entity_Relation_reco.main();


    }


}
