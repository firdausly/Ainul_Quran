package com.AinulQuran.repository;

import com.AinulQuran.model.Malay_translation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Malay_translationRepository extends JpaRepository <Malay_translation, Integer > {

    Malay_translation findBySuraAndAya(int Sura,int Aya);

    @Cacheable(value="surah")
    List<Malay_translation> findBySura(int Sura);

    @Cacheable(value="surahall")
    List<Malay_translation> findAll();

    int countBySura (int surah);


}
