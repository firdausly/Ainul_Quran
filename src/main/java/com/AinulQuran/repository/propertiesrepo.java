package com.AinulQuran.repository;

import com.AinulQuran.model.properties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface propertiesrepo extends JpaRepository<properties,Integer> {



    List<properties> findAll();


    properties findByName(String name);


}
