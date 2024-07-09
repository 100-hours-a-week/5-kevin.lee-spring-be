package org.example.spring_be.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.example.spring_be.dto.LoginRequestDTO;
import org.example.spring_be.dto.LoginResponseDTO;
import org.example.spring_be.dto.SignupRequestDTO;
import org.example.spring_be.model.Userinfo;
import org.example.spring_be.service.UserService;
import org.example.spring_be.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO signupRequestDTO){
        Optional<Userinfo> userinfoOptional = userService.signupUser(signupRequestDTO);

        //회원가입 실패 대응
        if(userinfoOptional.isEmpty()){
            return ResponseEntity.internalServerError().body("internal server error");
        }

        return ResponseEntity.ok("signup success");
    }


}
