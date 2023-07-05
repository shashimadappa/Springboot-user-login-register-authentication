package com.example.authentication.dto;

import lombok.Data;

@Data
public class Credentials {
    private String username;

    private String password;

    private String email;
}
