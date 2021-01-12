package com.AinulQuran.repository;

import com.AinulQuran.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository < User, Long > {
    @Cacheable(value="username")
    User findByUsername(String username);

    @Cacheable(value="email")
    User findByEmail(String email);

    @Cacheable(value="id")
    Optional<User> findById(Long id);

    void deleteByUsername(String username);

    void deleteByRoles(String roles);

}
