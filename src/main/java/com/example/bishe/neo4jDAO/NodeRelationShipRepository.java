package com.example.bishe.neo4jDAO;

import com.example.bishe.entity.relation;
import org.springframework.data.neo4j.repository.Neo4jRepository;

//新版neo4j不需要使用relation的Repository
public interface NodeRelationShipRepository extends Neo4jRepository<relation,Long> {

}
