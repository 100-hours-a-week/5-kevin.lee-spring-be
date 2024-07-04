package org.example.spring_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String message;
    private String token;

    public LoginResponseDTO(String message, String token){
        this.message = message;
        this.token = token;
    }
}
