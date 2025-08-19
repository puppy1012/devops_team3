package com.example.devops_team3.review.controller.dto.response.review;

import java.time.LocalDateTime;

public class RegisterReviewResponse {
    private Long reviewId;
    private Long accountId;
    private Long placeId;
    public String title;
    public String description;
    public LocalDateTime createdAt;


    public RegisterReviewResponse(Long reviewId, Long accountId, Long placeId, String title, String description, String string) {
        this.reviewId = reviewId;
        this.accountId = accountId;
        this.placeId = placeId;
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
    public Long getReviewId() {
        return reviewId;
    }
    public Long getAccountId() {
        return accountId;
    }
    public Long getPlaceId() {return placeId;}
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

