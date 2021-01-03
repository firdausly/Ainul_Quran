package com.AinulQuran.repository;

import com.AinulQuran.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository < User, Long > {
    User findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findById(Long id);

    void deleteByUsername(String username);

    void deleteByRoles(String roles);

}
