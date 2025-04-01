package com.example.bishe.neo4jDAO;

import com.example.bishe.entity.entity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface NodeRepository extends Neo4jRepository<entity, Long> {

    //按照name查找结点
    @Query("MATCH (node) " +
            "WHERE node.name = $name " +
            "RETURN node")
    entity findFirstByName(String name);

    //根据名字找到所有关系
    @Query(value = "MATCH (n:entity {name: $name})-[r]-(m) RETURN { id: id(r), entity: m, relation: type(r) }")
    List<Object> findRelationsByName(String name);

    //根据id添加关系:
    @Query("MATCH (a) WHERE id(a) = $id1 " +
            "MATCH (b) WHERE id(b) = $id2 " +
            "CALL apoc.create.relationship(a, $relation, {title: $title}, b) YIELD rel " +
            "RETURN rel")
    void addRelation(Long id1, String relation, Long id2, String title);


}