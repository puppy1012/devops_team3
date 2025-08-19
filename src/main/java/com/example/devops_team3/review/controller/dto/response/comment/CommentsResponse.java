package com.example.devops_team3.review.controller.dto.response.comment;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public class CommentsResponse {
   private Long id;
   private Long accountId;
   private String content;
   private LocalDateTime createdAt;

   public Long getId() {
       return id;
   }
   public void setId(Long id) {
       this.id = id;
   }
   public Long getAccountId() {
       return accountId;
   }
   public void setAccountId(Long accountId) {
       this.accountId = accountId;
   }
   public String getContent() {
       return content;
   }
   public void setContent(String content) {
       this.content = content;
   }
   public LocalDateTime getCreatedAt() {
       return createdAt;
   }
   public void setCreatedAt(LocalDateTime createdAt) {
       this.createdAt = createdAt;
   }

}
