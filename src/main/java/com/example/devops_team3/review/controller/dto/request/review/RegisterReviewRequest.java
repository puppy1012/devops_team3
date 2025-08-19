package com.example.devops_team3.review.controller.dto.request.review;

public class RegisterReviewRequest {
    private Long placeId;
    private String title;
    private String description;

    public RegisterReviewRequest() {}
    public RegisterReviewRequest(Long placeId, String title, String description) {
        this.placeId = placeId;
        this.title = title;
        this.description = description;
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
}
