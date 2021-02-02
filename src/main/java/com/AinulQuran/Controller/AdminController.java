package com.AinulQuran.Controller;

import com.AinulQuran.model.*;
import com.AinulQuran.repository.*;
import com.AinulQuran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private propertiesrepo propertiesrepo;

    @Autowired
    private UserPaymentRepo paymentRepo;

    @Autowired
    private Malay_translationRepository translationRepository;






    @GetMapping("/admin")
    public String admin(Model model ){

        List<userPayment> all=paymentRepo.findAll();

        double amount = 0;
        for(int i=0;i<all.size();i++){
            String amountreceived=all.get(i).getAmountreceived();
            if(amountreceived==null){
                continue;
            }
             amount+=Double.parseDouble(amountreceived);
        }




        long totalUser=service.count();

        int countadmin=0;
        int countuser=0;
        int countpaid=0;

        List<User> allUser=service.findAll();
        for(int i=0;i<allUser.size();i++){
            String roles=allUser.get(i).getRoles().toString();

            boolean rolesContainsAdmin=roles.contains("ROLE_ADMIN");
            boolean rolesContainsPaid=roles.contains("ROLE_PAID");
            boolean rolesContainsUser=roles.contains("ROLE_USER");

            if(rolesContainsAdmin && rolesContainsPaid && rolesContainsUser){
                countadmin+=1;
            }

            if(!rolesContainsAdmin && rolesContainsPaid && rolesContainsUser){
                countpaid+=1;
            }

            if(!rolesContainsAdmin && !rolesContainsPaid && rolesContainsUser){
                countuser+=1;
            }
        }

        model.addAttribute("countadmin",countadmin);
        model.addAttribute("countpaid",countpaid);
        model.addAttribute("countuser",countuser);
        model.addAttribute("UserInfo",totalUser);
        model.addAttribute("revenue",amount);

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


        if(wbwList.isEmpty()){
            return "admin/listSurah";
        }

        model.addAttribute("wbwDetail", wbwList.get());
        return "admin/listWbw";
    }

    @PostMapping("/admin/saveWbw")
    public String saveWbw(Model model,@ModelAttribute("wbwDetail") wbw wbw) {


        if(wbwRepository.findById(wbw.getId()).isEmpty()){
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


            userPayment userPayments=paymentRepo.findByUsername(username);

            //delete user payment info too
            if(userPayments!=null){
                paymentRepo.deleteByUsername(username);
            }

            //delete user
            userRepository.deleteByUsername(username);





            String success="User "+username+" has been successfully deleted ";
            model.addAttribute("successMessage",success);

        }


        return "admin/listUser";
    }


    @GetMapping("/admin/editUserDetail/{email}")
    public String editUserDetail(@PathVariable("email") String email,Model model){

//        Optional<User> currentUserOptional=service.find);
//        User currentUser = currentUserOptional.get();
//
        User currentUser = service.findByEmail(email);
        if(currentUser==null){
            return "redirect:/admin/listUser";
        }
        model.addAttribute("userDetail",currentUser);
        return "admin/editUserDetail";
    }

    @Transactional
    @PostMapping("/admin/saveUserDetail")
    public String save(Model model,@ModelAttribute("user") User user) {


        Optional<User> currentUser=service.findById(user.getId());


        //set the password first before deleting
        currentUser.ifPresent(value -> user.setPassword(value.getPassword()));




        //check if user is normal but has data in payment repo
        if(!user.getRoles().toString().contains("ROLE_PAID")){
            paymentRepo.deleteByUsername(user.getUsername());
        }

        //delete first to delete all the existing roles too
        currentUser.ifPresent(value -> userRepository.deleteByUsername(value.getUsername()));
        //then save a new instance.. if using edit the existing data in the roles table
        //will not be deleted.

        if(userRepository.findByUsername(user.getUsername()) ==null){
            service.normalSave(user);
        }


//        service.edit(user);




        return "redirect:/admin/editUserDetail/"+user.getEmail()+"?success";
    }


    @GetMapping("/admin/listProperties")
    public String listProperties(Model model){
        List<properties> propertiesList= propertiesrepo.findAll();

        model.addAttribute("propertiesList", propertiesList);
        return "admin/listProperties";
    }

    @GetMapping("/admin/editProperties/{name}")
    public String editProperties(@PathVariable("name") String name ,Model model){

       name=name.trim();
       properties currentProperties=propertiesrepo.findByName(name);

        if(currentProperties==null){
            return "redirect:/admin/listProperties";
        }



        model.addAttribute("propertiesDetail", currentProperties);
        return "admin/listProperties";
    }


    @PostMapping("/admin/saveProperties")
    public String saveProperties(Model model,@ModelAttribute("propertiesDetail") properties currentProperties) {


        if(propertiesrepo.findByName(currentProperties.getName())==null){
            model.addAttribute("errorMessage","Error, Cant find that surah");
            return "/admin/listProperties";
        }
        propertiesrepo.save(currentProperties);
        return "redirect:/admin/listProperties"+"?success";
    }


    @GetMapping("/admin/listPayment")
    public String listPayment(Model model){
        List<userPayment> paymentList= paymentRepo.findAll();

        model.addAttribute("paymentList", paymentList);
        return "admin/listPayment";
    }





    @GetMapping("/admin/listTranslation")
    public String listTranslationPerayat(Model model){

        List<Malay_translation> allTranslation=translationRepository.findAll();

        model.addAttribute("translationList", allTranslation);
        return "admin/listTranslation";
    }

    @GetMapping("/admin/editTranslation/{id}")
    public String editTranslation(@PathVariable("id") int ayat,Model model){


        Optional<Malay_translation> translation=translationRepository.findById(ayat);

        if(translation.isEmpty()){
            return "redirect:/admin/listTranslation";
        }



        model.addAttribute("translationDetail", translation);
        return "admin/listTranslation";
    }


    @PostMapping("/admin/saveTranslation")
    public String saveTranslation(Model model,@ModelAttribute("translationDetail") Malay_translation translation) {

        System.out.println(translation);
        Optional<Malay_translation> choosentranslation=translationRepository.findById(translation.getId());
        System.out.println(choosentranslation);
        if(choosentranslation.isEmpty()){
            model.addAttribute("errorMessage","Error, Cant find that surah");
            return "/admin/listTranslation";
        }
        translationRepository.save(translation);
        return "redirect:/admin/listTranslation"+"?success";
    }



}
