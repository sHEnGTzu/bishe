package com.example.bishe.service;

import com.example.bishe.neo4jDAO.NodeRepository;
import com.example.bishe.entity.entity;
import com.example.bishe.entity.relation;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.value.MapValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NodeServiceImpl implements NodeService{

    @Autowired
    private NodeRepository NodeRepository;

    @Autowired
    private Driver neo4jDriver;

    //创建结点建立关系或者找到已有结点并建立关系
    @Override
    public void createRelationBetweenNodes(String nodeName1,String nodeName2,String relation,String title){
        //尝试寻找第一个
        entity entity1 = this.findNodeByName(nodeName1);
        //如不存在，则先创建
        if (entity1 == null){
            entity1 = createEntity(nodeName1,title);
        }
        //尝试寻找第二个
        entity entity2 = this.findNodeByName(nodeName2);
        //如不存在，则先创建
        if (entity2 == null){
            entity2 = createEntity(nodeName2,title);
        }
        //创建关系
        createRelation(entity1,entity2,relation,title);

    }

    //创建结点
    @Override
    public entity createEntity(String name,String title){
        //如果结点已存在，则直接返回
        entity entity = findNodeByName(name);
        if (entity != null) return entity;

        entity = new entity();
        entity.setName(name);
        entity.setTitle(title);
        NodeRepository.save(entity);
        return entity;
    }

    //创建带有类型的结点
    @Override
    public entity createEntity(String name,String type,String title){
        //如果结点已存在，则直接返回
        entity entity = findNodeByName(name);
        if (entity != null) return entity;

        entity = new entity();
        entity.setName(name);
        entity.setType(type);
        entity.setTitle(title);
        NodeRepository.save(entity);
        return entity;
    }

    //查找结点
    @Override
    public entity findNodeByName(String name) {
        entity _temp = NodeRepository.findFirstByName(name);
        if (_temp == null) return null;


        List<Object> temp = NodeRepository.findRelationsByName(name);
        for (Object obj: temp){
            relation relation1 = new relation();
            if (obj instanceof org.neo4j.driver.internal.value.MapValue) {
                MapValue mapValue = (org.neo4j.driver.internal.value.MapValue) obj;
                Map<String, Object> map = mapValue.asMap();
                InternalNode entity = (InternalNode) map.get("entity");
                String _name = String.valueOf(entity.get("name"));

                int start = _name.indexOf("\"") + 1;
                int end = _name.lastIndexOf("\"");
                if (start < end) {
                    _name = _name.substring(start, end);
                }

                Long id = (Long) map.get("id");
                entity target = NodeRepository.findFirstByName(_name);
                String relation = (String) map.get("relation");
                String title = (String) map.get("title");

                relation1.setId(id);
                relation1.setEntity(target);
                relation1.setRelation(relation);
                relation1.setTitle(title);


            }
            if (relation1.getEntity() != null) _temp.getRelation().add(relation1);
        }

        return _temp;
    }

    //创建关系
    @Override
    public void createRelation(entity entity1, entity entity2, String relationName, String title) {

        NodeRepository.addRelation(entity1.getId(),relationName,entity2.getId(),title);

    }

    @Override
    public List<Map<String, String>> getAllTriples(String papername) {
        List<Map<String, String>> triples = new ArrayList<>();

        String cypher = """
        MATCH (a)-[r]->(b)
        WHERE a.title = $papername AND b.title = $papername
        RETURN a.name AS start, type(r) AS relation, b.name AS end
    """;

        try (Session session = neo4jDriver.session()) {
            session.readTransaction(tx -> {
                Result result = tx.run(cypher, Map.of("papername", papername));
                while (result.hasNext()) {
                    Record record = result.next();
                    Map<String, String> triple = new HashMap<>();
                    triple.put("start", record.get("start").asString());
                    triple.put("relation", record.get("relation").asString());
                    triple.put("end", record.get("end").asString());
                    triples.add(triple);
                }
                return null;
            });
        }

        return triples;
    }



}
