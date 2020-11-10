package com.AinulQuran.repository;

import com.AinulQuran.model.Malay_translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Malay_translationRepository extends JpaRepository <Malay_translation, Integer > {

    Malay_translation findBySuraAndAya(int Sura,int Aya);

    List<Malay_translation> findBySura(int Sura);
    List<Malay_translation> findAll();


}
