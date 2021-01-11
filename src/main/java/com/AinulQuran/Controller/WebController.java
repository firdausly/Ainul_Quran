package com.AinulQuran.Controller;


import com.AinulQuran.*;
import com.AinulQuran.model.*;
import com.AinulQuran.repository.*;
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




    @GetMapping("/search")
    public String search(Model model){

        List<surahindexes> allSurah=surahindexrepo.findAll();
        model.addAttribute("surahList",allSurah);

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
    public String surahview(@PathVariable("chapter") int chapter, Model model)  {

        int totalAyatcount;
        List<wbw> wbwperayat;

        List<Malay_translation> malay_translations;

        totalAyatcount= Math.toIntExact(malayrepo.countBySura(chapter));



        malay_translations=malayrepo.findBySura(chapter);

        Map<String, List<wbw>> map = new LinkedHashMap<String,List<wbw>>();

        for(int i=0;i<totalAyatcount;i++){

            int ayat=i+1;
            wbwperayat=wbwrepo.findBychapterAndAyat(chapter,ayat);
            wbw arabicnumeric=new wbw();
            arabicnumeric.setWordarabic(toArabicNum(ayat));
            arabicnumeric.setWordenglish(""+ayat+"");
            wbwperayat.add(arabicnumeric);
            map.put(malay_translations.get(i).getText(),wbwperayat);
        }

        model.addAttribute("Surah",map);

        surahindexes surahdetail=surahindexrepo.findBysurano(chapter);

        model.addAttribute("surahDetail",surahdetail);

        model.addAttribute("chapter",chapter);

        return "surahview";
    }

    public String toArabicNum(int n){
        String ArabicNum=n+"";
        ArabicNum=ArabicNum.replace("0","۰");
        ArabicNum=ArabicNum.replace("1","١");
        ArabicNum=ArabicNum.replace("2","٢");
        ArabicNum=ArabicNum.replace("3","٣");
        ArabicNum=ArabicNum.replace("4","٤");
        ArabicNum=ArabicNum.replace("5","٥");
        ArabicNum=ArabicNum.replace("6","٦");
        ArabicNum=ArabicNum.replace("7","٧");
        ArabicNum=ArabicNum.replace("8","٨");
        ArabicNum=ArabicNum.replace("9","٩");


        ArabicNum="﴾" +ArabicNum;

        ArabicNum=ArabicNum+"﴿";


        return ArabicNum;
    }






}
