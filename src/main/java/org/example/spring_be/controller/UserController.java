package org.example.spring_be.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.spring_be.dto.user.*;
import org.example.spring_be.model.Userinfo;
import org.example.spring_be.service.UserService;
import org.example.spring_be.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    private Optional<Userinfo> getOptionalUserinfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userService.getUserByEmail(email);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest, HttpServletResponse httpServletResponse) {
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

    @GetMapping("/profile")
    public ResponseEntity<GetUserDataDTO> getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Userinfo> optionalUserinfo = userService.getUserByEmail(email);

        if(optionalUserinfo.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            Userinfo userinfo = optionalUserinfo.get();
            GetUserDataDTO getUserDataDTO = new GetUserDataDTO(userinfo.getEmail(), userinfo.getNickname());
            return ResponseEntity.ok().body(getUserDataDTO);
        }
    }

    @PatchMapping("/profile")
    public ResponseEntity<GetUserDataDTO> updateUserData(@RequestBody UpdateUserRequestDTO updatedInfo) {
        Optional<Userinfo> optionalUserinfo = getOptionalUserinfo();

        if(optionalUserinfo.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Userinfo userinfo = optionalUserinfo.get();
        Userinfo updatedUser = userService.updateUserNickname(userinfo.getUserId(), updatedInfo);

        GetUserDataDTO getUserDataDTO = new GetUserDataDTO(updatedUser.getEmail(), updatedUser.getNickname());
        return ResponseEntity.ok(getUserDataDTO);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteUser(){
        Optional<Userinfo> optionalUserinfo = getOptionalUserinfo();

        if(optionalUserinfo.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Userinfo userinfo = optionalUserinfo.get();
        Userinfo result = userService.deleteUser(userinfo.getUserId());

        if(!result.isEnabled()) {
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.internalServerError().build();
        }
    }
    @PatchMapping("/profile/password")
    public ResponseEntity<String> alterPassword(@RequestBody AlterPasswordDTO request){
        Optional<Userinfo> optionalUserinfo = getOptionalUserinfo();

        if(optionalUserinfo.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Userinfo userinfo = optionalUserinfo.get();
        Long userId = userinfo.getUserId();

        Userinfo result = userService.alterPassword(userId, request.getPassword());
        if(!result.getPassword().equals(request.getPassword())){
            return ResponseEntity.ok().body("alter success");
        }else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/nickname/check")
    public ResponseEntity<String> checkNickDup(@RequestParam String nickname){

        System.out.println("nickname = " + nickname);
        if(userService.checkNickDup(nickname)){
            System.out.println("userService = " + userService.checkNickDup(nickname));
            return ResponseEntity.badRequest().body("already_exist_nickname");
        }else{
            return ResponseEntity.ok().build();
        }
    }
}

