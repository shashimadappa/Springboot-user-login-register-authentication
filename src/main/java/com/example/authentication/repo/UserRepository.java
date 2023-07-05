package com.example.authentication.repo;

import com.example.authentication.model.UserSignup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserSignup, Integer> {

    public UserSignup findByUsername(String username);

    public UserSignup findByEmail(String email);
}