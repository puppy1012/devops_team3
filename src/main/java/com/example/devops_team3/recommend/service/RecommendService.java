// com.example.devops_team3.recommend.service.RecommendService
package com.example.devops_team3.recommend.service;

import com.example.devops_team3.account.service.AccountIdentityService;
import com.example.devops_team3.place_data.controller.request.PlaceSearchRequest;
import com.example.devops_team3.place_data.entity.Place;
import com.example.devops_team3.place_data.service.PlaceSearchService;
import com.example.devops_team3.recommend.dto.response.PlaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class RecommendService {

    // ⛔ FeignClient 제거
    // private final AccountClient accountClient;
    // private final PlaceClient placeClient;

    // ⭕ 내부 패키지 서비스 직접 주입
    private final AccountIdentityService accountIdentityService;
    private final PlaceSearchService placeSearchService;

    /** 키워드 기반 추천 목록 */
    public List<PlaceResponseDto> recommendListKeyword(String authorization, PlaceSearchRequest req) {
        accountIdentityService.accountId(authorization); // 인증/식별만 수행
        return placeSearchService.search(req).stream()
                .map(PlaceResponseDto::from)
                .toList();
    }

    /** 지역 기반 추천 목록 (요청 DTO가 동일하면 위 메서드 재사용 가능) */
    public List<PlaceResponseDto> recommendListLocation(String authorization, PlaceSearchRequest req) {
        accountIdentityService.accountId(authorization);
        return placeSearchService.search(req).stream()
                .map(PlaceResponseDto::from)
                .toList();
    }

    /** 키워드 결과 중 랜덤 1건 추천 */
    public PlaceResponseDto recommendRandom(String authorization, PlaceSearchRequest req) {
        accountIdentityService.accountId(authorization);
        List<Place> places = placeSearchService.search(req);
        if (places == null || places.isEmpty()) {
            return new PlaceResponseDto(null, "추천 장소가 없습니다.", "", "", "", "");
        }
        Place pick = places.get(ThreadLocalRandom.current().nextInt(places.size()));
        return PlaceResponseDto.from(pick);
    }
}
