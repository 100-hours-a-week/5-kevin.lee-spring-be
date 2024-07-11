package org.example.spring_be.dto.user;

import lombok.Data;

@Data
public class SignupRequestDTO {
    private String email;
    private String password;
    private String nickname;
}
