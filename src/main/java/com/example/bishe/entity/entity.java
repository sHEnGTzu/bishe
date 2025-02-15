package com.example.bishe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.io.Serializable;
import java.util.List;

@Data
@org.springframework.data.neo4j.core.schema.Node(primaryLabel = "entity")
@Builder
@NoArgsConstructor // 添加无参构造函数
@AllArgsConstructor // 添加全参构造函数
public class entity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")
    private String name;

    @Relationship(type = "relation", direction = Relationship.Direction.INCOMING)
    private List<com.example.bishe.entity.relation> relation;


}
