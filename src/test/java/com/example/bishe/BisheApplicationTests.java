package com.example.bishe;

import com.example.bishe.dao.NodeRepository;
import com.example.bishe.entity_reco.Entity_Relation_reco;
import com.example.bishe.ollama.aiInter;
import com.example.bishe.service.NodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class BisheApplicationTests {

    @Autowired
    NodeRepository NodeRepository;

    @Autowired
    NodeService nodeService;

    @Test
    public void testCreate(){
//        Optional<person> byId = personRepository.findById(3L);
//        byId.orElse(null);
//
//        person person = new person();
//        person.setName("Marie");
//
//        personRepository.save(person);



//        for (personRelationShip temp:person1.get().getPersonRelationShip()){
//            //遍历关系的方法
//        }


//        Optional<Node> person1 = NodeRepository.findById(0L);  //出发节点序号为0
//        Optional<Node> person2 = NodeRepository.findById(1L);  //结束节点序号为1
//
//        //添加两节点之间的关系
//        person1.get().getRelation().add(Relation.builder().Node(person2.get()).relation("夫妻").build());
//
//        NodeRepository.save(person1.get());



//        aiInter aiInter = new aiInter();
////        System.out.println(aiInter.tiwen("知识图谱上的推理是预测不完整三元组中缺失的实体或关系，对结构化知识进行补全，并用于" +
////                "不同下游任务的过程。不同于被普遍研究的黑盒方法，如基于表示学习的推理方法，基于规则抽取的推理方法通过" +
////                "从知识图谱中泛化出一阶逻辑规则，实现一种可解释的推理范式。请你从上面这段话中提取出所有可能的实体，并估计该实体" +
////                "作为实体的可能性大小，你的回答格式必须为：结果是[实体1:97%，实体2：70%，...]，直接给答案无需说明。"));
//        System.out.println(aiInter.tiwen("知识图谱上的推理是预测不完整三元组中缺失的实体或关系，对结构化知识进行补全，" +
//                "并用于不同下游任务的过程。不同于被普遍研究的黑盒方法" +
//                "，如基于表示学习的推理方法，基于规则抽取的推理方法通过从知识图谱中泛化出一阶逻辑规则，" +
//                "实现一种可解释的推理范式。请你从上面这段话中提取出所有可能的实体，并估计该实体作为实体的可能性大小，" +
//                "你的回答格式必须为：结果是[实体1:97%，实体2：70%，...]，不要有“其他”这种实体，直接给答案无需说明。"));


        //创建结点及关系测试
//        System.out.println(nodeService.createEntity("小明"));
//        System.out.println(nodeService.createEntity("小红"));
//        System.out.println(nodeService.createEntity("小张"));
//        System.out.println(nodeService.createEntity("小李"));
//        nodeService.createRelationBetweenNodes("小红","小张","同事");
//        nodeService.createRelationBetweenNodes("小红","小李","同事");
//        nodeService.createRelationBetweenNodes("小红","小明","邻居");
//        nodeService.createRelationBetweenNodes("小明","小张","邻居");
//
//        System.out.println(nodeService.findNodeByName("小红"));
//        System.out.println(nodeService.findNodeByName("小明"));
//        System.out.println(nodeService.findNodeByName("小张"));
//        System.out.println(nodeService.findNodeByName("小李"));


        Entity_Relation_reco entity_Relation_reco = new Entity_Relation_reco("C:\\Users\\24204\\Desktop\\paper\\1.pdf","deepseek-r1:1.5b");
        System.out.println(entity_Relation_reco.sentences.size());
        entity_Relation_reco.main();

    }

}
