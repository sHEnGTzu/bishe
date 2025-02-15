package com.example.bishe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;


@Data
@RelationshipProperties
@Builder
@NoArgsConstructor // 添加无参构造函数
@AllArgsConstructor // 添加全参构造函数
public class relation {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private entity entity;

    @Property("relation")
    private String relation;



}
