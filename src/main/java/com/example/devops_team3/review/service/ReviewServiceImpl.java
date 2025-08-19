package com.example.devops_team3.review.service;

import com.example.devops_team3.review.controller.dto.request.review.RegisterReviewRequest;
import com.example.devops_team3.review.controller.dto.request.review.UpdateReviewRequest;
import com.example.devops_team3.review.controller.dto.response.review.RegisterReviewResponse;
import com.example.devops_team3.review.controller.dto.response.review.ReviewResponse;
import com.example.devops_team3.review.entity.Review;
import com.example.devops_team3.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    //register
    @Override
    public RegisterReviewResponse registerReview(RegisterReviewRequest registerRequest, Long accountId) {
        Review review = new Review(
                accountId,
                registerRequest.getPlaceId(),
                registerRequest.getTitle(),
                registerRequest.getDescription(),
                LocalDateTime.now()
        );

        Review savedReview = reviewRepository.save(review);
        return new RegisterReviewResponse(
                savedReview.getId(),
                savedReview.getAccountId(),
                savedReview.getPlaceId(),
                savedReview.getTitle(),
                savedReview.getDescription(),
                savedReview.getCreatedAt().toString()

        );
    }

    //read
    @Override
    public ReviewResponse readReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));


        return ReviewResponse.builder()
                .id(review.getId())
                .accountId(review.getAccountId())
                .placeId(review.getPlaceId())
                .title(review.getTitle())
                .description(review.getDescription())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Override
    public List<ReviewResponse> readByPlaceId(Long placeId) {
        List<Review> reviews = reviewRepository.findByPlaceId(placeId);

        return reviews.stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .accountId(review.getAccountId())
                        .placeId(review.getPlaceId())
                        .title(review.getTitle())
                        .description(review.getDescription())
                        .createdAt(review.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    //update
    @Override
    public ReviewResponse updateReview(Long id, UpdateReviewRequest updateRequest, Long accountId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 리뷰를 찾을 수 없습니다."));

        if (!review.getAccountId().equals(accountId)) {
            throw new RuntimeException("작성자만 리뷰 수정할 수 있습니다.");
        }

        if (updateRequest.getTitle() != null) {
            review.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null) {
            review.setDescription(updateRequest.getDescription());
        }

        Review updatedReview = reviewRepository.save(review);
        return new ReviewResponse(
                updatedReview.getId(),
                updatedReview.getAccountId(),
                updatedReview.getPlaceId(),
                updatedReview.getTitle(),
                updatedReview.getDescription(),
                updatedReview.getCreatedAt()
        );
    }

    //delete
    @Override
    public void deleteReview(Long id, Long accountId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 리뷰를 찾을 수 없습니다."));

        if (!review.getAccountId().equals(accountId)) {
            throw new RuntimeException("작성자만 리뷰 수정할 수 있습니다.");
        }
        reviewRepository.deleteById(id);
    }
}