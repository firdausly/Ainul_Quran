package com.AinulQuran.Controller;

import com.AinulQuran.model.User;
import com.AinulQuran.model.surahindexes;
import com.AinulQuran.model.user_vocab;
import com.AinulQuran.model.wbw;
import com.AinulQuran.repository.UserRepository;
import com.AinulQuran.repository.UserVocab;
import com.AinulQuran.repository.surahindexesrepo;
import com.AinulQuran.repository.wbwRepository;
import com.AinulQuran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserVocab userVocabRepository;

    @Autowired
    private wbwRepository wbwRepository;

    @Autowired
    private surahindexesrepo surahindexesrepo;



    @GetMapping("/admin")
    public String admin(Model model ){

        long totalUser=service.count();

        model.addAttribute("UserInfo",totalUser);

        return "admin/admin";
    }

    @GetMapping("/admin/listUser")
    public String listUser(Model model){
        List<User> allUser=service.findAll();

        model.addAttribute("UserInfo",allUser);
        return "admin/listUser";
    }

    @GetMapping("/admin/listWbw/{chapter}")
    public String listWbw(@PathVariable("chapter") int chapter,Model model){
        List<wbw> wbwList = wbwRepository.findBychapter(chapter);

        if(wbwList==null){
            return "admin/listSurah";
        }

        model.addAttribute("wbwList", wbwList);
        model.addAttribute("chapter", chapter);
        return "admin/listWbw";
    }

    @GetMapping("/admin/editWbwDetail/{id}")
    public String editWbwDetail(@PathVariable("id") int id,Model model){
        Optional<wbw> wbwList = wbwRepository.findById(id);


        if(wbwList==null){
            return "admin/listSurah";
        }

        model.addAttribute("wbwDetail", wbwList.get());
        return "admin/listWbw";
    }

    @PostMapping("/admin/saveWbw")
    public String saveWbw(Model model,@ModelAttribute("wbwDetail") wbw wbw) {


        if(wbwRepository.findById(wbw.getId())==null){
            model.addAttribute("errorMessage","Error, Cant find that Id");
            return "/admin/listSurah";
        }
        wbwRepository.save(wbw);
        model.addAttribute("id",wbw.getId());
        return "redirect:/admin/listWbw/"+wbw.getChapter()+"?success";
    }



    @GetMapping("/admin/listSurah")
    public String listWbw(Model model){
        List<surahindexes> surahList= surahindexesrepo.findAll();

        model.addAttribute("surahList", surahList);
        return "admin/listSurah";
    }

    @GetMapping("/admin/editSurah/{surano}")
    public String editSurah(@PathVariable("surano") int surano,Model model){


        surahindexes currentSurah = surahindexesrepo.findBysurano(surano);

        if(currentSurah==null){
            return "redirect:/admin/listSurah";
        }



        model.addAttribute("surahDetail", currentSurah);
        return "admin/listSurah";
    }


    @PostMapping("/admin/saveSurah")
    public String saveSurah(Model model,@ModelAttribute("surahDetail") surahindexes currentSurah) {


        if(surahindexesrepo.findBysurano(currentSurah.getSurano())==null){
            model.addAttribute("errorMessage","Error, Cant find that surah");
            return "/admin/listSurah";
        }
        surahindexesrepo.save(currentSurah);
        return "redirect:/admin/listSurah"+"?success";
    }



    @Transactional
    @GetMapping("/admin/removeUser/{username}")
    public String removeUser(@PathVariable("username") String username, Model model){


        User currentUser=service.findByUsername(username);


        if(currentUser==null ){
            String error="The User does not exist";
            model.addAttribute("errorMessage",error);
            return "admin/listUser";
        }
        else {
            List<user_vocab> user_vocab=userVocabRepository.findByUsername(username);

            if(!user_vocab.isEmpty()){
                userVocabRepository.deleteByUsername(username);
            }
            userRepository.deleteByUsername(username);


            String success="User "+username+" has been successfully deleted from your vocabulary list";
            model.addAttribute("successMessage",success);

        }


        return "admin/listUser";
    }


    @GetMapping("/admin/editUserDetail/{email}")
    public String editUserDetail(@PathVariable("email") String email,Model model){

//        Optional<User> currentUserOptional=service.findById(id);
//        User currentUser = currentUserOptional.get();
//        System.out.println(currentUser);
        User currentUser = service.findByEmail(email);
        model.addAttribute("userDetail",currentUser);
        return "admin/editUserDetail";
    }

    @Transactional
    @PostMapping("/admin/saveUserDetail")
    public String save(Model model,@ModelAttribute("user") User user) {


        Optional<User> currentUser=service.findById(user.getId());


        //set the password first before deleting
        user.setPassword(currentUser.get().getPassword());

        //delete first to delete all the existing roles too
        userRepository.deleteByUsername(currentUser.get().getUsername());

        //then save a new instance.. if using edit the existing data in the roles table
        //will not be deleted.

        if(userRepository.findByUsername(user.getUsername()) ==null){
            service.normalSave(user);
        }


//        service.edit(user);




        return "redirect:/admin/editUserDetail/"+user.getEmail()+"?success";
    }




}
