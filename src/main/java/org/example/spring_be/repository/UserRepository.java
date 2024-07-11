package org.example.spring_be.repository;

import org.example.spring_be.model.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Userinfo, Long> {
    Optional<Userinfo> findByEmail(String email);
    Optional<Userinfo> findByUserId(Long user_id);
    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);
}
