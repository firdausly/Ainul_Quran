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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


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

    @GetMapping("/edit")
    public String edit(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName(); //get logged in username

        User currentUser=service.findByUsername(currentUserName);
        model.addAttribute("user",currentUser);
        return "save";
    }

    @PostMapping("/save")
    public String save(Model model,@ModelAttribute("user") User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName(); //get logged in username

        User currentUser=service.findByUsername(currentUserName);
//        System.out.println(user);
        user.setPassword(currentUser.getPassword());
        user.setRoles(currentUser.getRoles());
        service.edit(user);
//        System.out.println(user);
        return "redirect:/edit?success";
    }


    @GetMapping("/profile")
    public String profile(Model model ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName(); //get logged in username

        User currentUser=service.findByUsername(currentUserName);

        model.addAttribute("UserInfo",currentUser);

        return "profile";
    }

    @GetMapping("/admin")
    public String admin(Model model ){

        long totalUser=service.count();

        model.addAttribute("UserInfo",totalUser);

        return "admin";
    }
    @GetMapping("/listUser")
    public String listUser(Model model ){

        List<User> allUser=service.findAll();

        model.addAttribute("UserInfo",allUser);

        return "listUser";
    }

    @GetMapping("/search")
    public String search(){
        return "search";
    }

    @GetMapping("/vocab/{vocab}")
    public String savevocab(@PathVariable("vocab") String vocabulary,Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();

        User currentUser=service.findByUsername(currentusername);
        user_vocab user_vocabulary=new user_vocab();
        user_vocab check=uservocabrepo.findByUsernameAndVocab(currentUser.getUsername(),vocabulary);
        user_vocabulary.setUsername(currentusername);
        user_vocabulary.setVocab(vocabulary);

        if(check!=null ){
            String error="The word already existed in your vocabulary!";
            model.addAttribute("errorMessage",error);
            return "vocab";
        }
        else {//save if not exist yet
//            System.out.println("save sepatotnye tapi kok null");
//            System.out.println(user_vocabulary);
                    uservocabrepo.save(user_vocabulary);
        }


        return "redirect:/vocab";
    }

    @GetMapping("/vocab")
    public String vocab(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();

        User currentUser=service.findByUsername(currentusername);
        List<user_vocab> listuservocabulary=uservocabrepo.findByUsername(currentusername);

        model.addAttribute("wbw",listuservocabulary);

        return "vocab";
    }

    @GetMapping("/vocab/viewword/{word}")
    public String viewword(@PathVariable("word") String word,Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();


        User currentUser=service.findByUsername(currentusername);
        List<user_vocab> listuservocabulary=uservocabrepo.findByUsername(currentusername);

        model.addAttribute("wbw",listuservocabulary);
        List<wbw> wordselected=wbwrepo.findByWordarabic(word);
        model.addAttribute("wordselected",wordselected);

        for(int i=0;i<wordselected.size();i++){
            int chapter=wordselected.get(i).getChapter();
            int ayat=wordselected.get(i).getAyat();
            int wordchoosen=wordselected.get(i).getWord();


        }


        ArrayList<Dataforchart> test=new ArrayList<Dataforchart>();
        model.addAttribute("word",word);
        int count=1;
        //fill up..
        for(int i=1;i<=114;i++){
            test.add(new Dataforchart(i+"",wbwrepo.countBychapterAndWordarabic(i,word)+""));
        }

        model.addAttribute("dataforchart",test);




        return "viewsameword";
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
        model.addAttribute("test",data);
        return "highlightword";
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
