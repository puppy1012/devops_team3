// com.example.devops_team3.place_data.service.PlaceSearchService
package com.example.devops_team3.place_data.service;

import com.example.devops_team3.place_data.controller.request.PlaceSearchRequest;
import com.example.devops_team3.place_data.entity.Place;
import com.example.devops_team3.place_data.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceSearchService {

    private final PlaceRepository placeRepository;

    public List<Place> search(PlaceSearchRequest request) {
        String title = request.getTitle();
        String category = request.getCategory();
        String location = request.getLocation();

        if (title != null && category != null && location != null) {
            return placeRepository.findByTitleContainingAndCategoryAndLocation(title, category, location);
        } else if (title != null && category != null) {
            return placeRepository.findByTitleContainingAndCategory(title, category);
        } else if (title != null && location != null) {
            return placeRepository.findByTitleContainingAndLocation(title, location);
        } else if (title != null) {
            return placeRepository.findByTitleContaining(title);
        } else if (category != null && location != null) {
            return placeRepository.findByCategoryAndLocation(category, location);
        } else if (category != null) {
            return placeRepository.findByCategory(category);
        } else if (location != null) {
            return placeRepository.findByLocation(location);
        } else {
            return placeRepository.findAll();
        }
    }
}
