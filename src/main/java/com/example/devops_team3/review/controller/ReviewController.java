package com.example.devops_team3.review.controller;

import com.example.devops_team3.account.service.AccountIdentityService;               // ★ 추가
import com.example.devops_team3.review.controller.dto.request.review.RegisterReviewRequest;
import com.example.devops_team3.review.controller.dto.request.review.UpdateReviewRequest;
import com.example.devops_team3.review.controller.dto.response.review.RegisterReviewResponse;
import com.example.devops_team3.review.controller.dto.response.review.ReviewResponse;
import com.example.devops_team3.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;                           // ★ 추가
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final AccountIdentityService accountIdentityService;                      // ★ Feign 대신 내부 서비스

    // 리뷰 생성
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<RegisterReviewResponse> registerReview(
            @RequestHeader("Authorization") String token,
            @RequestBody RegisterReviewRequest registerRequest
    ) {
        Long accountId = accountIdentityService.accountId(token);                    // ★ 한 줄로 인증/식별
        log.info("Authorization header: {}", token);

        RegisterReviewResponse reviewResponse = reviewService.registerReview(registerRequest, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse);
    }

    // 리뷰 단건 조회
    @GetMapping("/{id}")
    public ReviewResponse readReview(@PathVariable("id") Long id) {
        return reviewService.readReview(id);
    }

    // 장소별 리뷰 조회
    @GetMapping("/place/{placeId}")
    public List<ReviewResponse> readbyplaceId(@PathVariable("placeId") Long placeId) {
        return reviewService.readByPlaceId(placeId);
    }

    // 리뷰 수정
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateReviewRequest request
    ) {
        Long accountId = accountIdentityService.accountId(token);
        log.info("Authorization header: {}", token);

        ReviewResponse updatedReview = reviewService.updateReview(id, request, accountId);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteReview(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token
    ) {
        Long accountId = accountIdentityService.accountId(token);
        log.info("Authorization header: {}", token);

        reviewService.deleteReview(id, accountId);
        return ResponseEntity.ok().build();
    }
}
