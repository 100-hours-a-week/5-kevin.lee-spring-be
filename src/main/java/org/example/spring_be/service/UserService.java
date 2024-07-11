package org.example.spring_be.service;

import jakarta.transaction.Transactional;
import org.example.spring_be.dto.user.LoginRequestDTO;
import org.example.spring_be.dto.user.LoginResponseDTO;
import org.example.spring_be.dto.user.SignupRequestDTO;
import org.example.spring_be.dto.user.UpdateUserRequestDTO;
import org.example.spring_be.model.Authority;
import org.example.spring_be.model.UserRole;
import org.example.spring_be.model.Userinfo;
import org.example.spring_be.repository.AuthorityRepository;
import org.example.spring_be.repository.UserRepository;
import org.example.spring_be.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;





    @Lazy
    @Autowired
    public UserService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

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

    public Optional<Userinfo> signupUser(SignupRequestDTO signupRequestDTO){
        if(userRepository.existsByEmail(signupRequestDTO.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        } else if (userRepository.existsByNickname(signupRequestDTO.getNickname())) {
            throw new IllegalArgumentException("Nickname alreay exists");
        }

        Userinfo userinfo = new Userinfo();
        userinfo.setEmail(signupRequestDTO.getEmail());
        userinfo.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        userinfo.setNickname(signupRequestDTO.getNickname());
        userinfo.setEnabled(true);
        userinfo.setDeletedAt(null);

        Authority verifiedAuthority = authorityRepository.findByAuthority(UserRole.VERIFIED.getValue())
                .orElseGet(() -> {
                    Authority newAuthority = new Authority();
                    newAuthority.setAuthority(UserRole.VERIFIED.getValue());
                    return authorityRepository.save(newAuthority);
                });

        userinfo.setAuthorities(Collections.singleton(verifiedAuthority));


        Userinfo savedUserinfo = userRepository.save(userinfo);
        System.out.println("userinfo.toString() = " + userinfo.getCreatedAt());
        return Optional.of(savedUserinfo);
    }

    public Optional<Userinfo> getUserByEmail(String email) {
        return userRepository.findByEmail(email);

    }

    @Transactional
    public Userinfo updateUserNickname(Long user_id, UpdateUserRequestDTO updatedInfo){
        Userinfo userinfo = userRepository.findByUserId(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedInfo.getNickname() != null) {
            userinfo.setNickname(updatedInfo.getNickname());
        }

        return userRepository.save(userinfo);
    }

    public Boolean checkNickDup(String nickname){
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public Userinfo deleteUser(Long userId){
        Optional<Userinfo> optionalUserinfo = userRepository.findByUserId(userId);

        if(optionalUserinfo.isEmpty()){
            throw new RuntimeException("User not found");
        }else{
            Userinfo userinfo = optionalUserinfo.get();
            userinfo.setEnabled(false);
            userinfo.setDeletedAt(LocalDateTime.now());
            userRepository.save(userinfo);

            return userinfo;
        }
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Userinfo> userOptional = userRepository.findByEmail(email);
        Userinfo user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }


    public Userinfo alterPassword(Long userId, String password) {
        Optional<Userinfo> optionalUserinfo = userRepository.findByUserId(userId);

        if(optionalUserinfo.isEmpty()){
            throw new RuntimeException("not found");
        }else{
            Userinfo userinfo = optionalUserinfo.get();
            userinfo.setPassword(passwordEncoder.encode(password));
            return userRepository.save(userinfo);
        }

    }
}
