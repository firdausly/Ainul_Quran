package com.AinulQuran.repository;


import com.AinulQuran.model.wbw;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface wbwRepository extends JpaRepository<wbw, Integer > {

    @Cacheable(value="wbwsall")
    List<wbw> findAll();

    @Cacheable(value="wbws")
    List<wbw> findBychapter(int chapter);

    @Cacheable(value="wbwsarabic")
    List<wbw> findByWordarabic(String wordarabic);

    @Cacheable(value="wbwsayat")
    List<wbw> findBychapterAndAyat(int chapter,int Ayat);

    @Cacheable(value="wbwsayatnword")
    wbw findBychapterAndAyatAndWord(int chapter,int Ayat, int Word);

    int countByChapterAndAyat(int chapter,int Ayat);

    @Cacheable(value="wbwslong")
    Long countBychapterAndWordarabic(int chapter,String wordarabic);

    @Cacheable(value="wbwslongchapter")
    Long countBychapter(int chapter);

    @Cacheable(value="wbwsarabiclong")
    Long countByWordarabic(String wordarabic);

    wbw findTopByOrderByIdDesc();



    @Override
    @Caching(evict = {
            @CacheEvict(value="wbwsall",allEntries = true),
            @CacheEvict(value="wbws",allEntries = true),
            @CacheEvict(value="wbwsarabic",allEntries = true),
            @CacheEvict(value="wbwsayat",allEntries = true),
            @CacheEvict(value="wbwsayatnword",allEntries = true)
    })
    <S extends wbw> S save(S s);


    @Override
    @Caching(evict = {
            @CacheEvict(value="wbwsall",allEntries = true),
            @CacheEvict(value="wbws",allEntries = true),
            @CacheEvict(value="wbwsarabic",allEntries = true),
            @CacheEvict(value="wbwsayat",allEntries = true),
            @CacheEvict(value="wbwsayatnword",allEntries = true)
    })
    void deleteById(Integer integer);
}
