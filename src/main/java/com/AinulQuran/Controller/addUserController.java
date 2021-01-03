package com.AinulQuran.Controller;

import com.AinulQuran.dto.UserRegistrationDto;
import com.AinulQuran.model.User;
import com.AinulQuran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin/addUser")
public class addUserController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "addUser";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {

        User existing = userService.findByUsername(userDto.getUsername());
        User exist2=userService.findByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }

        if(exist2!=null){
            result.rejectValue("email",null,"There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            return "addUser";
        }

        userService.save(userDto);
        return "redirect:/admin/addUser?success";
    }
}