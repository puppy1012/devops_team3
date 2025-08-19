package com.example.devops_team3.review.controller.dto.response.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ReviewResponse {
    private Long id;
    private Long accountId;
    private Long placeId;
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;


    public ReviewResponse() {}


    public ReviewResponse(Long id, Long accountId, Long placeId, String title, String description, LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.placeId = placeId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }


    public Long getId() {
        return id;
    }
    public Long getAccountId() {
        return accountId;
    }
    public Long getPlaceId() {
        return placeId;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}