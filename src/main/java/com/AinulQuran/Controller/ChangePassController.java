package com.AinulQuran.Controller;

import com.AinulQuran.dto.PasswordDto;
import com.AinulQuran.dto.UserRegistrationDto;
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
@RequestMapping("/changepass")
public class ChangePassController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute("passwordchange")
    public PasswordDto passwords(){
        return new PasswordDto();
    }

    @GetMapping
    public String showChangepassForm(Model model) {
        return "profile/changepass";
    }

    @PostMapping
    public String Changepassword(Model model,@ModelAttribute("passwordchange") @Valid PasswordDto passDto,
                                      BindingResult result )  {


        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = auth.getName(); //get logged in username
            User currentUser=userService.findByUsername(currentUserName);
            boolean isMatches=passwordEncoder.matches(passDto.getCurrentPass(),currentUser.getPassword());
            if(!isMatches){
                result.rejectValue("currentPass",null,"Your current password does not match");

            }
            if (result.hasErrors()) {
                return "profile/changepass";
            }

            //set password
            currentUser.setPassword(passwordEncoder.encode(passDto.getConfirmPass()));

            //update password
            userService.edit(currentUser);


        } catch (NullPointerException e){
        }




        return "redirect:/changepass?success";
    }
}