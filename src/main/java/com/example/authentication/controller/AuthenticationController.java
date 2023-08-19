package com.example.authentication.controller;

import com.example.authentication.dto.Credentials;
import com.example.authentication.model.UserSignup;
import com.example.authentication.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.authentication.utils.Utils;
import com.example.authentication.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthenticationController {
    @Autowired
    UserRepository UserRepository;
    @Autowired
    UserService UserService;

    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationController(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/UserSignup")
    public ResponseEntity<?> registerUser(@RequestBody Credentials credentials) {
        System.out.println("triggred");
        return UserService.registerUser(credentials);
    }

    @PostMapping("/UserLogin")
    public ResponseEntity<?> loginUser(@RequestBody Credentials credentials) {

        System.out.println("triggred");
        return UserService.loginUser(credentials);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String token){
        String email = Utils.decodeToken(token);
        UserSignup userDetails = UserRepository.findByEmail(email);
        System.out.println(userDetails);
        return UserService.getAllUsers(userDetails);
    }

}
