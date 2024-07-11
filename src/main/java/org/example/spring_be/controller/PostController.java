package org.example.spring_be.controller;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.example.spring_be.dto.post.CreatePostDTO;
import org.example.spring_be.model.Postinfo;
import org.example.spring_be.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping("/")
    public ResponseEntity<String> createPost(@RequestBody CreatePostDTO request){
        Optional<Postinfo> optionalPostinfo = postService.createPost(request);

        if(optionalPostinfo.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body("createPost success");
    }

    @GetMapping("/")
    public ResponseEntity<String> readPosts(){


        return ResponseEntity.ok().body("");
    }

}
