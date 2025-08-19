package com.example.devops_team3.review.repository;

import com.example.devops_team3.review.controller.dto.response.review.ReviewResponse;
import com.example.devops_team3.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPlaceId(Long placeId);
}
