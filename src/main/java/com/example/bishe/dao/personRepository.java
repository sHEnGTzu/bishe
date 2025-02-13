package com.example.bishe.dao;

import com.example.bishe.entity.person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;


//当作mysql的Mapper层使用，是程序和数据库的接入处
@Repository
public interface personRepository extends Neo4jRepository<person,Long> {

}
