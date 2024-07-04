package org.example.spring_be.service;

import lombok.AllArgsConstructor;
import org.example.spring_be.model.Userinfo;
import org.example.spring_be.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Userinfo> createUser(String email, String password, String nickname){
        if(userRepository.findByNickname(nickname).isPresent()){
            return Optional.empty();
        }

        if(userRepository.findByEmail(email).isPresent()){
            return Optional.empty();
        }

        Userinfo userinfo = Userinfo.builder()
                .nickname(nickname)
                .email(email)
                .password(passwordEncoder.encode(password))
                .postinfos(new ArrayList<>())
                .commentinfos(new ArrayList<>())
                .build();

        userinfo = userRepository.save(userinfo);
        return Optional.of(userinfo);
    }

    public Optional<Userinfo> login(String email, String password) {
        Optional<Userinfo> userinfoOptional = userRepository.findByEmail(email);

        if(userinfoOptional.isPresent()){
            Userinfo userinfo = userinfoOptional.get();
            if(passwordEncoder.matches(password, userinfo.getPassword())){
                return Optional.of(userinfo);
            }
        }

        //로그인 실패
        return Optional.empty();
    }
}
