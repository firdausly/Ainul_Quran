package com.AinulQuran.repository;

import com.AinulQuran.model.Malay_translation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Malay_translationRepository extends JpaRepository <Malay_translation, Integer > {


    @Cacheable(value="surah")
    List<Malay_translation> findBySura(int Sura);

    @Cacheable(value="surahall")
    List<Malay_translation> findAll();

    int countBySura (int surah);

    @Override
    @Caching(evict = {
            @CacheEvict(value="surah",allEntries = true),
            @CacheEvict(value="surahall",allEntries = true)
    })
    <S extends Malay_translation> S save(S s);
}
