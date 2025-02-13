package com.example.bishe.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;


@Data
@RelationshipProperties
@Builder
public class personRelationShip {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private person person;

    @Property
    private String relation;
}
