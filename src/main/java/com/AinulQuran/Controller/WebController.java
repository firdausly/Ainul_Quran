package com.AinulQuran.Controller;


import com.AinulQuran.Ayatperayat;
import com.AinulQuran.Dataforchart;
import com.AinulQuran.Dataforhighlight;
import com.AinulQuran.Surah;
import com.AinulQuran.model.*;
import com.AinulQuran.repository.Malay_translationRepository;
import com.AinulQuran.repository.UserVocab;
import com.AinulQuran.repository.surahindexesrepo;
import com.AinulQuran.repository.wbwRepository;
import com.AinulQuran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.*;


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
        return "about";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }



    @GetMapping("/admin")
    public String admin(Model model ){

        long totalUser=service.count();

        model.addAttribute("UserInfo",totalUser);

        return "admin";
    }
    @GetMapping("/search")
    public String search(){
        return "search";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }


    @GetMapping("highlight")
    public String highlight(Model model){

        List<surahindexes> listindex=surahindexrepo.findAll();
//        Dataforhighlight data=new Dataforhighlight();
        List<Dataforhighlight> data = new ArrayList<>();

        //initialize table
        for(int i=1;i<=114;i++){
            List<wbw> listwbw=wbwrepo.findBychapter(i);
            ArrayList<String> inte=new ArrayList<>();
            for(int j=0;j<listwbw.size();j++){
                int chapter=listwbw.get(j).getChapter();
                int ayat=listwbw.get(j).getAyat();
                int word=listwbw.get(j).getWord();

                inte.add("c"+chapter+"a"+ayat+"w"+word);
            }
            data.add(new Dataforhighlight(i,inte));

        }




//        data.add(new Dataforhighlight(1,));
//        List<wbw> list=wbwrepo.findAll();
//        System.out.println(data);
//        System.out.println(data);
        model.addAttribute("test",data);
        return "highlightword";
    }

    @GetMapping("highlight/{word}")
    public String highlightword(@PathVariable("word") String words, Model model){


        List<wbw> wordselected=wbwrepo.findByWordarabic(words);
        ArrayList<String> listofhighlighted=new ArrayList<String>();
        Set<Integer> chapterset=new HashSet<Integer>();

        for(int i=0;i<wordselected.size();i++){
            int chapter=wordselected.get(i).getChapter();
            int ayat=wordselected.get(i).getAyat();
            int word=wordselected.get(i).getWord();
            listofhighlighted.add("c"+chapter+"a"+ayat+"w"+word);
            chapterset.add(wordselected.get(i).getChapter());
        }





        List<surahindexes> listindex=surahindexrepo.findAll();
//        Dataforhighlight data=new Dataforhighlight();
        List<Dataforhighlight> data = new ArrayList<>();
        ArrayList<Integer> chapterlist=new ArrayList<>(chapterset);
        //initialize table
        for(int i=0;i<chapterlist.size();i++){
            int chapterofcontainedword=chapterlist.get(i);
            List<wbw> listwbw=wbwrepo.findBychapter(chapterofcontainedword);
            ArrayList<String> inte=new ArrayList<>();
            for(int j=0;j<listwbw.size();j++){
                int chapter=listwbw.get(j).getChapter();
                int ayat=listwbw.get(j).getAyat();
                int word=listwbw.get(j).getWord();

                inte.add("c"+chapter+"a"+ayat+"w"+word);
            }
            data.add(new Dataforhighlight(chapterofcontainedword,inte));

        }




        model.addAttribute("highlightcss",listofhighlighted);
        model.addAttribute("test",data);
        model.addAttribute("word",words);
        return "highlightwordword";
    }

    @GetMapping("/surah/{chapter}")
    public String greeting(@PathVariable("chapter") int chapter, Model model)  {

        List<Malay_translation> malay=malayrepo.findBySura(chapter);
        if(chapter>114){
            return "redirect:/";
        }
        Surah x = new Surah(chapter,malay);

        //set repo from this controller to pass into Surah class
        //
        x.setWbwrepo(wbwrepo);

        Ayatperayat db=x.getSurah();

        model.addAttribute("surahName",x.getSurahName());
        model.addAttribute("Surah",db.map);

        return "surahview";
    }





}
