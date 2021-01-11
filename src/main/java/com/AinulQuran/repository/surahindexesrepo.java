package com.AinulQuran.repository;


import com.AinulQuran.model.surahindexes;
import com.AinulQuran.model.wbw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface surahindexesrepo extends JpaRepository<surahindexes, Integer > {

    surahindexes findBysurano(int surano);



}
