package org.example.spring_be.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String email;
    private String nickname;

    public LoginResponseDTO(String token, String email, String nickname) {
        this.token = token;
        this.email = email;
        this.nickname = nickname;
    }

    // Getters and Setters
}
