package com.example.devops_team3.review.controller.dto.request.review;

public class UpdateReviewRequest {
    private String title;
    private String description;

    public UpdateReviewRequest() {}

    public UpdateReviewRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
