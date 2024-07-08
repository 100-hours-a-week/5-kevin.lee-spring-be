package org.example.spring_be.service;

import org.example.spring_be.dto.LoginRequestDTO;
import org.example.spring_be.dto.LoginResponseDTO;
import org.example.spring_be.model.Userinfo;
import org.example.spring_be.repository.UserRepository;
import org.example.spring_be.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // Perform authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        final String token = jwtTokenUtil.generateToken(loginRequest.getEmail());

        // Load user details
        Optional<Userinfo> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            Userinfo user = userOptional.get();
            return new LoginResponseDTO(token, user.getEmail(), user.getNickname());
        } else {
            throw new RuntimeException("User not found");
        }
    }


}
