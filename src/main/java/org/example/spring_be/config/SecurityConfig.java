package org.example.spring_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable()) // CORS 비활성화, 필요에 따라 설정
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users/login", "/users/signup").permitAll() // 로그인 및 회원가입 엔드포인트는 인증 없이 접근 가능
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .formLogin(withDefaults()); // 기본 폼 로그인 사용, httpBasic 대신

        return http.build();
    }
}
