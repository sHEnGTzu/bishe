package com.example.bishe.service;

import com.example.bishe.dao.personRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class personService {

    @Autowired
    private personRepository personRepository;



}
