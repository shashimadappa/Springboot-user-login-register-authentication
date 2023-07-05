package com.example.authentication.utils;

import lombok.Data;

@Data
public class jwtResponse {

    private String token;
    private String type = "Bearer";
    private Integer id;
    private String username;
    private String email;


    public jwtResponse(String token, int id, String username, String email) {
        super();
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
