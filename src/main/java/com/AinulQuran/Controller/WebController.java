package com.AinulQuran.Controller;


import com.AinulQuran.model.Audio;
import com.AinulQuran.model.Malay_translation;
import com.AinulQuran.model.surahindexes;
import com.AinulQuran.model.wbw;
import com.AinulQuran.repository.Malay_translationRepository;
import com.AinulQuran.repository.UserVocab;
import com.AinulQuran.repository.surahindexesrepo;
import com.AinulQuran.repository.wbwRepository;
import com.AinulQuran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
public class WebController {

    @Autowired
    private UserService service;

    @Autowired
    private UserVocab uservocabrepo;

    @Autowired
    private surahindexesrepo surahindexrepo;

    @Autowired
    private Malay_translationRepository malayrepo;

    @Autowired
    private wbwRepository wbwrepo;



    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/about")
    public String about(){
        return "redirect:/#about";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }




    @GetMapping("/search")
    public String search(Model model){

        List<surahindexes> allSurah=surahindexrepo.findAll();
        model.addAttribute("surahList",allSurah);

        return "surah/search";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }





    @GetMapping("/surah/{chapter}")
    public String surahview(@PathVariable("chapter") int chapter, Model model)  {

        int totalAyatcount;
        List<wbw> wbwperayat;

        List<Malay_translation> malay_translations;

        totalAyatcount= surahindexrepo.findBysurano(chapter).getAyacount();



        malay_translations=malayrepo.findBySura(chapter);


        Map<String, List<wbw>> surahMap = new LinkedHashMap<String,List<wbw>>();

        for(int i=0;i<totalAyatcount;i++){

            int ayat=i+1;
            wbwperayat=wbwrepo.findBychapterAndAyat(chapter,ayat);
            surahMap.put(malay_translations.get(i).getText(),wbwperayat);

        }



        model.addAttribute("Surah",surahMap);

        surahindexes surahdetail=surahindexrepo.findBysurano(chapter);


        if(chapter>1 && chapter <= 114){
            int previousSurahAyatCount=0;

            //loop to sum all the previous ayah to get the audio for the current ayat
            //sumofpreviousayat + index of current ayat = current index of the ayat
            for(int i=1;i<chapter;i++){
                previousSurahAyatCount+=surahindexrepo.findBysurano(i).getAyacount();
            }


            model.addAttribute("previousSuraAyaCount",previousSurahAyatCount);

        }else {
            model.addAttribute("previousSuraAyaCount",0);
        }


        model.addAttribute("surahDetail",surahdetail);

        model.addAttribute("chapter",chapter);

        RestTemplate restTemplate= new RestTemplate();

        Audio audio = restTemplate.getForObject(
                "http://api.alquran.cloud/edition/format/audio", Audio.class);


        assert audio != null;
        model.addAttribute("editionDetails",audio.getData());

        return "surah/surahview";
    }








}
