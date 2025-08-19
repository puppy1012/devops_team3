package com.example.devops_team3.review.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    @Column(name = "place_id")
    private Long placeId;
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public Review() {}
    public Review(Long accountId, Long placeId, String title, String description, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.placeId = placeId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }
    public Long getId() {
        return id;
    }
    public  Long getAccountId() {
        return accountId;
    }
    public Long getPlaceId() {
        return placeId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

//    @OneToMany(mappedBy = "review")
//    private List<Comments> comments = new ArrayList<>();


}
