package com.example.authentication.service;

import com.example.authentication.dto.Credentials;
import com.example.authentication.model.UserSignup;
import com.example.authentication.repo.UserRepository;
import com.example.authentication.utils.Utils;
import com.example.authentication.utils.jwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserRepository UserRepository;

    public UserService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<?> registerUser(@RequestBody Credentials credentials) {
        if (UserRepository.findByUsername(credentials.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        if (UserRepository.findByEmail(credentials.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        UserSignup user = new UserSignup();
        user.setUsername(credentials.getUsername());
        user.setPassword(passwordEncoder.encode(credentials.getPassword()));
        user.setEmail(credentials.getEmail());
        UserRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    public ResponseEntity<?> loginUser(@RequestBody Credentials credentials){
        UserSignup user = UserRepository.findByUsername(credentials.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
        }

        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        String token = Utils.generateToken(user.getEmail(), user.getId());
        return ResponseEntity.ok(new jwtResponse(token, user.getId(), user.getUsername(), user.getEmail()));
    }

    public ResponseEntity<?> getAllUsers(UserSignup userDetails) {
        List<UserSignup> list = UserRepository.findAll();
        return  ResponseEntity.ok().body(list);
    }
}