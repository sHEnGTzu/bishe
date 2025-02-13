package com.example.bishe;

import com.example.bishe.dao.personRepository;
import com.example.bishe.entity.person;
import com.example.bishe.entity.personRelationShip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class BisheApplicationTests {

    @Autowired
    personRepository personRepository;

    @Test
    public void testCreate(){
//        Optional<person> byId = personRepository.findById(3L);
//        byId.orElse(null);
//
//        person person = new person();
//        person.setName("Marie");
//
//        personRepository.save(person);

//        for (personRelationShip temp:person1.get().getPersonRelationShip()){
//            //遍历关系的方法
//        }

        Optional<person> person1 = personRepository.findById(0L);  //出发节点序号为0
        Optional<person> person2 = personRepository.findById(1L);  //结束节点序号为1

        //添加两节点之间的关系
        person1.get().getPersonRelationShip().add(personRelationShip.builder().person(person2.get()).relation("夫妻").build());

        personRepository.save(person1.get());
    }

}
