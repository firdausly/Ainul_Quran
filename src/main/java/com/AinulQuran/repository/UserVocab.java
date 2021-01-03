package com.AinulQuran.repository;

import com.AinulQuran.model.user_vocab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserVocab extends JpaRepository<user_vocab,Long > {

    List<user_vocab> findByUsername(String x);

    user_vocab findByUsernameAndVocab(String username,String vocab);

    void deleteByUsernameAndVocab(String username,String vocab);

    void deleteByUsername(String username);


}
