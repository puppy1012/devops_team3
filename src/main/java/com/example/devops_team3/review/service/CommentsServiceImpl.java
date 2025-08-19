package com.example.devops_team3.review.service;

import com.example.devops_team3.review.controller.dto.request.comment.CommentsCreateRequest;
import com.example.devops_team3.review.controller.dto.request.comment.UpdateCommentsRequest;
import com.example.devops_team3.review.controller.dto.response.comment.CommentsCreateResponse;
import com.example.devops_team3.review.controller.dto.response.comment.CommentsResponse;
import com.example.devops_team3.review.entity.Comments;
import com.example.devops_team3.review.entity.Review;
import com.example.devops_team3.review.repository.CommentsRepository;
import com.example.devops_team3.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private ReviewRepository reviewRepository;


    @Override
    public CommentsCreateResponse createComments(Long reviewId, CommentsCreateRequest commentsRequest, Long accountId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("해당 리뷰를 찾을 수 없습니다."));

        // 2. CommentsCreateRequest와 accountId를 사용하여 Comments 엔티티를 생성
        Comments comments = Comments.builder()
                .accountId(accountId)
                .review(review) // Review 엔티티를 연결합니다.
                .content(commentsRequest.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        // 3. 생성된 Comments 엔티티를 저장소에 저장
        Comments savedComments = commentsRepository.save(comments);

        // 4. 저장된 엔티티를 기반으로 CommentsCreateResponse를 생성하여 반환
        return CommentsCreateResponse.builder()
                .id(savedComments.getId())
                .accountId(savedComments.getAccountId())
                .reviewId(savedComments.getReview().getId())
                .content(savedComments.getContent())
                .createdAt(savedComments.getCreatedAt())
                .build();
    }

    @Override
    public List<CommentsResponse> readComments(Long reviewId) {
       List<Comments> comments = commentsRepository.findByReviewId(reviewId);
        return comments.stream()
                .map(comment -> CommentsResponse.builder()
                        .id(comment.getId())
                        .accountId(comment.getAccountId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public CommentsResponse updateComments(Long id, UpdateCommentsRequest updateRequest, Long accountId) {
        Comments comments = commentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));
        if(!comments.getAccountId().equals(accountId)) {
            throw new RuntimeException("작성자만 댓글을 수정할 수 있습니다.");
        }
        comments.setContent(updateRequest.getContent());
        Comments updatedComments = commentsRepository.save(comments);
        return CommentsResponse.builder()
                .id(updatedComments.getId())
                .accountId(updatedComments.getAccountId())
                .content(updatedComments.getContent())
                .createdAt(updatedComments.getCreatedAt())
                .build();
    }

    @Override
    public void deleteComments(Long id, Long accountId) {
        Comments comments = commentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));
        if(!comments.getAccountId().equals(accountId)) {
            throw new RuntimeException("작성자만 댓글 삭제 가능합니다.");

        }
        commentsRepository.delete(comments);

    }
}
