package org.example.spring_be.dto.user;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;

}
