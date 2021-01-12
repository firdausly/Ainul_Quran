package com.AinulQuran.repository;


import com.AinulQuran.model.wbw;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface wbwRepository extends JpaRepository<wbw, Integer > {

    @Cacheable(value="wbws")
    List<wbw> findBychapter(int chapter);

    @Cacheable(value="wbwsarabic")
    List<wbw> findByWordarabic(String wordarabic);

    @Cacheable(value="wbwsayat")
    List<wbw> findBychapterAndAyat(int chapter,int Ayat);

    int countByChapterAndAyat(int chapter,int Ayat);

    @Cacheable(value="wbwslong")           // it will cache result and key name will be "employees"
    Long countBychapterAndWordarabic(int chapter,String wordarabic);

    @Cacheable(value="wbwslongchapter")           // it will cache result and key name will be "employees"
    Long countBychapter(int chapter);

    @Cacheable(value="wbwsarabiclong")           // it will cache result and key name will be "employees"
    Long countByWordarabic(String wordarabic);
}
