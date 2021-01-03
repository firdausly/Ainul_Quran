package com.AinulQuran.Controller;

import com.AinulQuran.model.User;
import com.AinulQuran.model.user_vocab;
import com.AinulQuran.repository.UserRepository;
import com.AinulQuran.repository.UserVocab;
import com.AinulQuran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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



    @GetMapping("/admin/listUser")
    public String listUser(Model model){
        List<User> allUser=service.findAll();

        model.addAttribute("UserInfo",allUser);
        return "listUser";
    }

    @Transactional
    @GetMapping("/admin/removeUser/{username}")
    public String removeUser(@PathVariable("username") String username, Model model){


        User currentUser=service.findByUsername(username);


        if(currentUser==null ){
            String error="The User does not exist";
            model.addAttribute("errorMessage",error);
            return "listUser";
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


        return "listUser";
    }


    @GetMapping("/admin/editUserDetail/{email}")
    public String editUserDetail(@PathVariable("email") String email,Model model){

//        Optional<User> currentUserOptional=service.findById(id);
//        User currentUser = currentUserOptional.get();
//        System.out.println(currentUser);
        User currentUser = service.findByEmail(email);
        model.addAttribute("userDetail",currentUser);
        return "editUserDetail";
    }

    @Transactional
    @PostMapping("/admin/saveUserDetail")
    public String save(Model model,@ModelAttribute("user") User user) {


        Optional<User> currentUser=service.findById(user.getId());




//        System.out.println(currentUser.get().getRoles());
//        System.out.println(user.getRoles());

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
