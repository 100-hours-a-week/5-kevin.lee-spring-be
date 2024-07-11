package org.example.spring_be.repository;

import org.example.spring_be.model.Postinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Postinfo, Long> {

}
