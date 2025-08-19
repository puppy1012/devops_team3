package com.example.devops_team3.review.service;

import com.example.devops_team3.review.controller.dto.request.comment.CommentsCreateRequest;
import com.example.devops_team3.review.controller.dto.request.comment.UpdateCommentsRequest;
import com.example.devops_team3.review.controller.dto.response.comment.CommentsCreateResponse;
import com.example.devops_team3.review.controller.dto.response.comment.CommentsResponse;

import java.util.List;

public interface CommentsService {
    CommentsCreateResponse createComments(Long reviewId, CommentsCreateRequest commentsRequest,Long accountId);

    CommentsResponse updateComments(Long id, UpdateCommentsRequest updateRequest, Long accountId);

    void deleteComments(Long id, Long accountId);

    List<CommentsResponse> readComments(Long reviewId);
}
