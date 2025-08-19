package com.example.devops_team3.review.controller.dto.response;

import lombok.Data;

@Data
public class SearchPlaceResponse {
    private Long placeId;
    private String title;
    private String content;
    private String category;
    private String location;
    private String address;

    public SearchPlaceResponse(Long placeId, String title, String content, String category, String location, String address) {
        this.placeId = placeId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.location = location;
        this.address = address;

    }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getCategory() {
            return category;
        }

        public String getLocation() {
            return location;
        }

        public String getAddress() {
            return address;
        }
    }



