package com.example.devops_team3.review.controller.dto.request.comment;

public class CommentsCreateRequest {
   private String content;

   public CommentsCreateRequest(String content) {
       this.content = content;
   }
  public String getContent() {
       return content;
  }
  public void setContent(String content) {
       this.content = content;
  }
}
