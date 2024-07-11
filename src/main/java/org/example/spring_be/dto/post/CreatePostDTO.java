package org.example.spring_be.dto.post;

import lombok.Data;

@Data
public class CreatePostDTO {
    private String post_title;
    private String post_content;
    private Long user_id;

}
