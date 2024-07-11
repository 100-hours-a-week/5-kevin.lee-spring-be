package org.example.spring_be.dto.user;

import lombok.Data;

@Data
public class GetUserDataDTO {
    private String email;
    private String nickname;

    public GetUserDataDTO(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
