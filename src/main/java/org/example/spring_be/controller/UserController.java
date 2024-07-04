package org.example.spring_be.controller;

import lombok.AllArgsConstructor;
import org.example.spring_be.model.Userinfo;
import org.example.spring_be.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.OptionPaneUI;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password){
        Optional<Userinfo> userinfoOptional = userService.login(email, password);

        if(userinfoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // 로그인 성공 시 처리
        return ResponseEntity.ok("Login successful");
    }


    @PostMapping("signup")
    public ResponseEntity<String> signup(String email, String password, String nickname){
        //service에서 회원 가입 처리해주는 것 필요
        Optional<Userinfo> userinfoOptional = userService.createUser(email, password, nickname);
        //회원가입 실패시 대응
        if(userinfoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일 혹은 닉네임입니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("로그인 성공");
    }

    @GetMapping("/check/email")
    public ResponseEntity<String> emailCheck(){


        return ResponseEntity.ok("Check email Successful");
    }

    @GetMapping("check/nickname")
    public ResponseEntity<String> nickCheck(){


        return ResponseEntity.ok("Check email Successful");
    }



    @GetMapping("/profile")
    public ResponseEntity<String> fetchUserData(){
        //service에서 유저 데이터 가져오기

        //유저 데이터 가져오는 거 실패 처리

        return ResponseEntity.ok("Fetch user data successful");
    }


    @PatchMapping("/profile")
    public void updateUser() {

    }

    @PatchMapping("/profile/password")
    public ResponseEntity<String> changePassword(){

        return ResponseEntity.ok("Fetch user data successful");
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteUser(){

        return ResponseEntity.ok("Fetch user data successful");
    }

}
