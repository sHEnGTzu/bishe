package com.example.bishe.dao;

import com.example.bishe.entity.entity;
import com.example.bishe.entity.relation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import javax.management.relation.Relation;
import java.util.Collection;
import java.util.List;


//当作mysql的Mapper层使用，是程序和数据库的接入处
@Repository
public interface NodeRepository extends Neo4jRepository<entity,Long> {

    //按照name查找结点
    @Query("MATCH (node) " +
            "WHERE node.name = $name " +
            "RETURN node")
    entity findFirstByName(String name);


    //根据名字找到所有关系
    @Query(value = "MATCH (n:entity {name: $name})-[r]-(m) RETURN { id: id(r), entity: m, relation: r.relation }")
    List<Object> findRelationsByName(String name);


    //根据id添加关系:
    @Query(value = "MATCH (a), (b)\n" +
            "WHERE id(a) = $id1 AND id(b) = $id2\n" +
            "CREATE (a)-[r:relation {relation: $relation}]->(b)")
    void addRelation(Long id1,String relation,Long id2);

}
