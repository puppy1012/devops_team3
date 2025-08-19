package com.example.devops_team3.review.service;

import com.example.devops_team3.review.controller.dto.request.review.RegisterReviewRequest;
import com.example.devops_team3.review.controller.dto.request.review.UpdateReviewRequest;
import com.example.devops_team3.review.controller.dto.response.review.RegisterReviewResponse;
import com.example.devops_team3.review.controller.dto.response.review.ReviewResponse;

import java.util.List;

public interface ReviewService {
    RegisterReviewResponse registerReview(RegisterReviewRequest registerRequest,Long accountId);

    ReviewResponse readReview(Long id);

    List<ReviewResponse> readByPlaceId(Long placeId);

    ReviewResponse updateReview(Long id, UpdateReviewRequest updateRequest,Long accountId);

    void deleteReview(Long id, Long accountId);
}
