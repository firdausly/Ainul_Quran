package com.AinulQuran.service;


import com.AinulQuran.dto.UserRegistrationDto;
import com.AinulQuran.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    long count();

    User edit(User user);

    User save(UserRegistrationDto registration);
}