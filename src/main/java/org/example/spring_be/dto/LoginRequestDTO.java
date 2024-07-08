package org.example.spring_be.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;

    // Getters and Setters
}
