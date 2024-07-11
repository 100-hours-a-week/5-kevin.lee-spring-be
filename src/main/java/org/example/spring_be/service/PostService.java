package org.example.spring_be.service;

import jakarta.transaction.Transactional;
import org.example.spring_be.dto.post.CreatePostDTO;
import org.example.spring_be.model.Postinfo;
import org.example.spring_be.model.Userinfo;
import org.example.spring_be.repository.PostRepository;
import org.example.spring_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Autowired
    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public Optional<Postinfo> createPost(CreatePostDTO createPostDTO) {
        Optional<Userinfo> optionalUserinfo = userRepository.findByUserId(createPostDTO.getUser_id());

        if(optionalUserinfo.isPresent()){
            Userinfo userinfo = optionalUserinfo.get();
            Postinfo postinfo = Postinfo.builder()
                    .userinfo(userinfo)
                    .postTitle(createPostDTO.getPost_title())
                    .postContent(createPostDTO.getPost_content())
                    .hits(0)
                    .likes(0)
                    .comments(0)
                    .build();

            Postinfo save = postRepository.save(postinfo);
            return Optional.of(save);
        }else{
            return Optional.empty();
        }
    }
}
