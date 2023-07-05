package com.example.authentication.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserSignup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

}
