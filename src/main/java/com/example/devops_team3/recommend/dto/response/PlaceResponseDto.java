package com.example.devops_team3.recommend.dto.response;

import com.example.devops_team3.place_data.entity.Place;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

// 불변 DTO (원하면 Lombok 대신 record 사용 가능)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceResponseDto {

    @JsonProperty("place_id")   // JSON 키는 place_id로 유지
    private final Long placeId;
    private final String title;
    private final String content;
    private final String category;
    private final String location;
    private final String address;

    public PlaceResponseDto(Long placeId, String title, String content,
                            String category, String location, String address) {
        this.placeId = placeId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.location = location;
        this.address = address;
    }

    // 엔티티 -> DTO 매핑 단일 진입점
    public static PlaceResponseDto from(Place p) {
        return new PlaceResponseDto(
                p.getPlace_id(),
                p.getTitle(),
                p.getContent(),
                p.getCategory(),
                p.getLocation(),
                p.getAddress()
        );
    }

    public Long getPlaceId() { return placeId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getAddress() { return address; }
}
