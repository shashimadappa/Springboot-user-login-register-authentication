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
    UserRepository userRepository;

    public UserService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<?> registerUser(@RequestBody Credentials credentials) {
        try {
            if (userRepository.findByUsername(credentials.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }

            if (userRepository.findByEmail(credentials.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }

            UserSignup user = new UserSignup();
            user.setUsername(credentials.getUsername());
            user.setPassword(passwordEncoder.encode(credentials.getPassword()));
            user.setEmail(credentials.getEmail());
            userRepository.save(user);

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }

    public ResponseEntity<?> loginUser(@RequestBody Credentials credentials) {
        try {
            UserSignup user = userRepository.findByUsername(credentials.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
            }

            if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }

            String token = Utils.generateToken(user.getEmail(), user.getId());
            return ResponseEntity.ok(new jwtResponse(token, user.getId(), user.getUsername(), user.getEmail()));
        } catch (Exception e) {
            // Handle any unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    public ResponseEntity<?> getAllUsers(UserSignup userDetails) {
        List<UserSignup> list = userRepository.findAll();
        return ResponseEntity.ok().body(list);
    }
}