package com.AinulQuran.service;


import com.AinulQuran.dto.UserRegistrationDto;
import com.AinulQuran.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findAll();

    long count();

    User edit(User user);

    User save(UserRegistrationDto registration);

    User normalSave(User user);
}