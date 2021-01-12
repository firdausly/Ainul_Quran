package com.AinulQuran.repository;


import com.AinulQuran.model.surahindexes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface surahindexesrepo extends JpaRepository<surahindexes, Integer > {

    @Cacheable(value="surahindexes")
    surahindexes findBysurano(int surano);

    @Cacheable(value="surahindexesall")
    List<surahindexes> findAll();



}
