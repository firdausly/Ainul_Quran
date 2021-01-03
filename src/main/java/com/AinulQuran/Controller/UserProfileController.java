package com.AinulQuran.Controller;

import com.AinulQuran.dto.PasswordDto;
import com.AinulQuran.model.User;
import com.AinulQuran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class UserProfileController {
    @Autowired
    private UserService service;

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
}