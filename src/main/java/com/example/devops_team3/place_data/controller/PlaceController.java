package com.example.devops_team3.place_data.controller;

import com.example.devops_team3.account.service.AccountIdentityService; // ★ 추가
import com.example.devops_team3.place_data.controller.request.PlaceSearchRequest;
import com.example.devops_team3.place_data.controller.request.RegisterPlaceWithAuthorizationRequest;
import com.example.devops_team3.place_data.controller.request.UpdatePlaceWithAuthorizationRequest;
import com.example.devops_team3.place_data.controller.response.RegisterPlaceWithAuthorizationResponse;
import com.example.devops_team3.place_data.controller.response.SearchPlaceResponse;
import com.example.devops_team3.place_data.controller.response.UpdatePlaceResponse;
import com.example.devops_team3.place_data.entity.Place;
import com.example.devops_team3.place_data.repository.PlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = {"http://localhost:5000","http://localhost:5001"})
@RestController
@RequestMapping("/place")
public class PlaceController {

    private final PlaceRepository placeRepository;
    private final AccountIdentityService accountIdentityService; // ★ 주입

    public PlaceController(PlaceRepository placeRepository,
                           AccountIdentityService accountIdentityService) {
        this.placeRepository = placeRepository;
        this.accountIdentityService = accountIdentityService;
    }

    // 여행지 등록
    @PostMapping("/register")
    public RegisterPlaceWithAuthorizationResponse register(
            @RequestHeader("Authorization") String token,
            @RequestBody RegisterPlaceWithAuthorizationRequest request) {

        log.info("Received request to register a new place");

        // ⬇️ Feign 호출 제거 → 내부 서비스 직접 호출
        Long accountId = accountIdentityService.accountId(token);

        Place requestedPlace = request.toRegister(accountId);
        Place registeredPlace = placeRepository.save(requestedPlace);
        return RegisterPlaceWithAuthorizationResponse.from(registeredPlace, "여행지가 성공적으로 등록되었습니다.");
    }

    // 리스트 조회 (변경 없음)
    @GetMapping("/list")
    public List<SearchPlaceResponse> listPlaces() {
        log.info("Received request to list all places");
        return placeRepository.findAll().stream()
                .map(SearchPlaceResponse::from)
                .collect(Collectors.toList());
    }

    // 단건 조회 (변경 없음)
    @GetMapping("/{place_id}")
    public SearchPlaceResponse getPlaceById(@PathVariable Long place_id) {
        log.info("get place by id: {}", place_id);
        Place place = placeRepository.findById(place_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장소를 찾지 못했습니다."));
        return SearchPlaceResponse.from(place);
    }

    @PostMapping("/search")
    public List<SearchPlaceResponse> searchPlaces(@RequestBody PlaceSearchRequest req) {
        String title = req.getTitle();
        String category = req.getCategory();
        String location = req.getLocation();

        List<Place> result;
        if (title != null && category != null && location != null) {
            result = placeRepository.findByTitleContainingAndCategoryAndLocation(title, category, location);
        } else if (title != null && category != null) {
            result = placeRepository.findByTitleContainingAndCategory(title, category);
        } else if (title != null && location != null) {
            result = placeRepository.findByTitleContainingAndLocation(title, location);
        } else if (title != null) {
            result = placeRepository.findByTitleContaining(title);
        } else if (category != null && location != null) {
            result = placeRepository.findByCategoryAndLocation(category, location);
        } else if (category != null) {
            result = placeRepository.findByCategory(category);
        } else if (location != null) {
            result = placeRepository.findByLocation(location);
        } else {
            result = placeRepository.findAll();
        }
        return SearchPlaceResponse.from(result);
    }


    // 수정
    @PostMapping("/update")
    public UpdatePlaceResponse updatePlace(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdatePlaceWithAuthorizationRequest request) {

        log.info("Received request to update a place");

        Long accountId = accountIdentityService.accountId(token);  // ★ 여기만 교체

        Long place_id = request.getPlace_id();
        if (place_id == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "place_id가 null입니다.");

        Place found = placeRepository.findById(place_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "수정할 여행지를 찾을 수 없습니다."));

        if (found.getAccountId() == null || !found.getAccountId().equals(accountId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "여행지를 수정할 권한이 없습니다.");
        }

        found.setTitle(request.getTitle());
        found.setContent(request.getContent());
        found.setCategory(request.getCategory());
        found.setLocation(request.getLocation());
        found.setAddress(request.getAddress());

        Place updated = placeRepository.save(found);
        return UpdatePlaceResponse.from(updated);
    }

    // 삭제
    @DeleteMapping("/delete/{place_id}")
    public ResponseEntity<?> deletePlace(
            @RequestHeader("Authorization") String token,
            @PathVariable Long place_id) {

        log.info("Received request to delete a place");

        Long accountId = accountIdentityService.accountId(token); // ★ 여기만 교체

        Place found = placeRepository.findById(place_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장소를 찾을 수 없습니다."));

        if (!accountId.equals(found.getAccountId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "여행지를 삭제할 권한이 없습니다.");
        }

        placeRepository.delete(found);
        return ResponseEntity.ok("여행지를 성공적으로 삭제했습니다.");
    }
}
