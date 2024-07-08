package org.example.spring_be.controller;

import org.example.spring_be.dto.LoginRequestDTO;
import org.example.spring_be.dto.LoginResponseDTO;
import org.example.spring_be.model.Userinfo;
import org.example.spring_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }


}
