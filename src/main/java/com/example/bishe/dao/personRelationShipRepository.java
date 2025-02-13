package com.example.bishe.dao;

import com.example.bishe.entity.personRelationShip;
import org.springframework.data.neo4j.repository.Neo4jRepository;

//新版neo4j不需要使用relation的Repository
public interface personRelationShipRepository extends Neo4jRepository<personRelationShip,Long> {

}
