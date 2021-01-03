package com.AinulQuran.repository;


import com.AinulQuran.model.dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Dictionary;
import java.util.List;

@Repository
public interface dictionaryrepo extends JpaRepository<dictionary, Integer> {


    dictionary findByword(String word);

    dictionary findBywordp(String wordp);
}
