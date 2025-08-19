package com.example.devops_team3.recommend.controller;

import com.example.devops_team3.place_data.controller.request.PlaceSearchRequest;
import com.example.devops_team3.recommend.dto.response.PlaceResponseDto;
import com.example.devops_team3.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/test")
    public String test() {
        log.info("test is processing");
        return "test is processing";
    }

    @PostMapping("/list/keyword")
    public ResponseEntity<List<PlaceResponseDto>> recommendListKeyword(
            @RequestHeader("Authorization") String token,
            @RequestBody PlaceSearchRequest requestDto
    ) {
        return ResponseEntity.ok(recommendService.recommendListKeyword(token, requestDto));
    }

    @PostMapping("/list/location")
    public ResponseEntity<List<PlaceResponseDto>> recommendListLocation(
            @RequestHeader("Authorization") String token,
            @RequestBody PlaceSearchRequest requestDto
    ) {
        return ResponseEntity.ok(recommendService.recommendListLocation(token, requestDto));
    }

    @PostMapping("/random")
    public ResponseEntity<PlaceResponseDto> recommendRandom(
            @RequestHeader("Authorization") String token,
            @RequestBody PlaceSearchRequest keywordDto
    ) {
        return ResponseEntity.ok(recommendService.recommendRandom(token, keywordDto));
    }
}
