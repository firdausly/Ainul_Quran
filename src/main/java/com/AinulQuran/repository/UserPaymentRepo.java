package com.AinulQuran.repository;

import com.AinulQuran.model.userPayment;
import com.AinulQuran.model.user_vocab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserPaymentRepo extends JpaRepository<userPayment,Long > {


    userPayment findByUsername(String Username);

    void deleteByUsername(String Username);

}
