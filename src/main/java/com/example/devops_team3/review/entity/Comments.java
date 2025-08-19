package com.example.devops_team3.review.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;


import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long accountId;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public Comments() {}


    public Long getId() {
        return id;
    }
    public Long getAccountId() {
        return accountId;
    }
   public String getContent() {
        return content;
   }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Review getReview() {
        return review;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
