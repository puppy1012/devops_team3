package com.example.devops_team3.review.controller.dto.response.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentsCreateResponse {
    private Long id;
    private Long accountId;
    private Long reviewId;
    private String content;
    private LocalDateTime createdAt;

}
