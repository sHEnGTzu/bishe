package com.example.bishe.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.io.Serializable;
import java.util.List;

@Data
@Node(primaryLabel = "person")
public class person implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String name;
    @Relationship(type = "personRelationShip", direction = Relationship.Direction.INCOMING)
    private List<personRelationShip> personRelationShip;


}
