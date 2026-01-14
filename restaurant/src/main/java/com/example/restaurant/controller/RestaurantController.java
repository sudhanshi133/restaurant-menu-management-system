package com.example.restaurant.controller;

import com.example.restaurant.model.dto.CreateRestaurantRequest;
import com.example.restaurant.model.dto.RestaurantResponse;
import com.example.restaurant.model.dto.UpdateRestaurantStatusRequest;
import com.example.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
     RestaurantResponse response = restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) {
        RestaurantResponse response = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RestaurantResponse> updateRestaurantStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRestaurantStatusRequest request) {
        RestaurantResponse response = restaurantService.updateRestaurantStatus(id, request);
        return ResponseEntity.ok(response);
    }
}

