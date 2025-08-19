package com.example.devops_team3.review.repository;

import com.example.devops_team3.review.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    List<Comments> findByReviewId(Long reviewId);
}
