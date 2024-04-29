package com.example.userdemo.repository;

import com.example.userdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    


    List<User> findAllByBirthdayBetween (LocalDate from, LocalDate to);
}
