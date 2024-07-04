package org.example.spring_be.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "postinfo")
public class Postinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_title", nullable = false)
    private String postTitle;

    @Column(name = "post_content", nullable = false)
    private String postContent;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @Column(name = "hits", columnDefinition = "INT DEFAULT 0")
    private Integer hits;

    @Column(name = "likes", columnDefinition = "INT DEFAULT 0")
    private Integer likes;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Userinfo userinfo;
}
