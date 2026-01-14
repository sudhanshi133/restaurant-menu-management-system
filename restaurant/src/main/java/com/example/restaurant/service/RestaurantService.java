package com.example.restaurant.service;

import com.example.restaurant.exception.DuplicateResourceException;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.model.dto.CreateRestaurantRequest;
import com.example.restaurant.model.dto.RestaurantResponse;
import com.example.restaurant.model.dto.UpdateRestaurantStatusRequest;
import com.example.restaurant.model.entity.Restaurant;
import com.example.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        if (restaurantRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Restaurant with name '" + request.getName() + "' already exists");
        }
        
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setLocation(request.getLocation());
        restaurant.setOpen(true);
        
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        
        return mapToResponse(savedRestaurant);
    }

    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        
        return mapToResponse(restaurant);
    }

    @Transactional
    public RestaurantResponse updateRestaurantStatus(Long id, UpdateRestaurantStatusRequest request) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        
        restaurant.setOpen(request.getIsOpen());
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        
        return mapToResponse(updatedRestaurant);
    }

    @Transactional(readOnly = true)
    public boolean isRestaurantOpen(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
        
        return restaurant.isOpen();
    }

    @Transactional(readOnly = true)
    public Restaurant getRestaurantEntityById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
    }

    private RestaurantResponse mapToResponse(Restaurant restaurant) {
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .location(restaurant.getLocation())
                .isOpen(restaurant.isOpen())
                .build();
    }
}

