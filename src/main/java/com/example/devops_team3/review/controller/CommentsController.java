package com.example.devops_team3.review.controller;

import com.example.devops_team3.account.service.AccountIdentityService; // ★ 추가
import com.example.devops_team3.review.controller.dto.request.comment.CommentsCreateRequest;
import com.example.devops_team3.review.controller.dto.request.comment.UpdateCommentsRequest;
import com.example.devops_team3.review.controller.dto.response.comment.CommentsCreateResponse;
import com.example.devops_team3.review.controller.dto.response.comment.CommentsResponse;
import com.example.devops_team3.review.service.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;           // ★ 트랜잭션
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor                                 // ★ 생성자 주입
@RequestMapping("/review/{reviewId}/comments")
public class CommentsController {

    private final CommentsService commentsService;
    private final AccountIdentityService accountIdentityService;  // ★ Feign 대신 내부 서비스

    // create comments
    @PostMapping
    @Transactional
    public ResponseEntity<CommentsCreateResponse> createComment(
            @PathVariable Long reviewId,
            @RequestBody CommentsCreateRequest commentsRequest,
            @RequestHeader("Authorization") String token
    ) {
        Long accountId = accountIdentityService.accountId(token); // ★ 한 줄로 인증/식별
        CommentsCreateResponse commentsResponse =
                commentsService.createComments(reviewId, commentsRequest, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentsResponse);
    }

    // read all comments of a review
    @GetMapping
    public ResponseEntity<List<CommentsResponse>> readComments(@PathVariable Long reviewId) {
        List<CommentsResponse> comments = commentsService.readComments(reviewId);
        return ResponseEntity.ok(comments);
    }

    // update comments
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<CommentsResponse> updateComments(
            @PathVariable Long reviewId,
            @PathVariable("id") Long id,
            @RequestBody UpdateCommentsRequest updateRequest,
            @RequestHeader("Authorization") String token
    ) {
        Long accountId = accountIdentityService.accountId(token);
        CommentsResponse updated = commentsService.updateComments(id, updateRequest, accountId);
        return ResponseEntity.ok(updated);
    }

    // delete comments
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteComments(
            @PathVariable Long reviewId,
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token
    ) {
        Long accountId = accountIdentityService.accountId(token);
        commentsService.deleteComments(id, accountId);
        return ResponseEntity.ok().build();
    }
}
