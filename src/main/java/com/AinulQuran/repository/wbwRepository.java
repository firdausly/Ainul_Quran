package com.AinulQuran.repository;


import com.AinulQuran.model.wbw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface wbwRepository extends JpaRepository<wbw, Integer > {

    List<wbw> findBychapter(int chapter);

    List<wbw> findByWordarabic(String wordarabic);

    List<wbw> findBychapterAndAyat(int chapter,int Ayat);

    int countByChapterAndAyat(int chapter,int Ayat);

    Long countBychapterAndWordarabic(int chapter,String wordarabic);

    Long countBychapter(int chapter);

    Long countByWordarabic(String wordarabic);
}
