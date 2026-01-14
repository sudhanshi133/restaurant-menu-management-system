package com.example.restaurant.service;

import com.example.restaurant.exception.DuplicateResourceException;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.exception.RestaurantClosedException;
import com.example.restaurant.model.dto.CreateMenuItemRequest;
import com.example.restaurant.model.dto.MenuItemResponse;
import com.example.restaurant.model.dto.UpdateMenuItemStatusRequest;
import com.example.restaurant.model.entity.MenuItem;
import com.example.restaurant.model.entity.Restaurant;
import com.example.restaurant.model.enums.Category;
import com.example.restaurant.model.enums.Status;
import com.example.restaurant.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    
    private final MenuItemRepository menuItemRepository;
    private final RestaurantService restaurantService;
    
    @Transactional
    public MenuItemResponse addMenuItem(Long restaurantId, CreateMenuItemRequest request) {
        Restaurant restaurant = restaurantService.getRestaurantEntityById(restaurantId);
        
        if (!restaurant.isOpen()) {
            throw new RestaurantClosedException("Cannot add menu items to a closed restaurant");
        }
        
        if (menuItemRepository.existsByNameAndRestaurantId(request.getName(), restaurantId)) {
            throw new DuplicateResourceException(
                "Menu item with name '" + request.getName() + "' already exists for this restaurant"
            );
        }
        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setStatus(Status.AVAILABLE);
        menuItem.setRestaurant(restaurant);
        menuItem.setDeleted(false);
        
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        
        return mapToResponse(savedMenuItem);
    }
    
    @Transactional(readOnly = true)
    public Page<MenuItemResponse> getMenuItems(
            Long restaurantId,
            Category category,
            Status status,
            Pageable pageable) {
        restaurantService.getRestaurantEntityById(restaurantId);
        
        Page<MenuItem> menuItems;
        if (category != null && status != null) {
            menuItems = menuItemRepository.findByRestaurantIdAndCategoryAndStatus(
                restaurantId, category, status, pageable
            );
        } else if (category != null) {
            menuItems = menuItemRepository.findByRestaurantIdAndCategory(
                restaurantId, category, pageable
            );
        } else if (status != null) {
            menuItems = menuItemRepository.findByRestaurantIdAndStatus(
                restaurantId, status, pageable
            );
        } else {
            menuItems = menuItemRepository.findByRestaurantId(restaurantId, pageable);
        }
        
        return menuItems.map(this::mapToResponse);
    }
    
    @Transactional
    public MenuItemResponse updateMenuItemStatus(Long menuItemId, UpdateMenuItemStatusRequest request) {
        MenuItem menuItem = menuItemRepository.findByIdAndNotDeleted(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuItemId));
        menuItem.setStatus(request.getStatus());
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        
        return mapToResponse(updatedMenuItem);
    }
    
    @Transactional
    public void deleteMenuItem(Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findByIdAndNotDeleted(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuItemId));
        menuItem.setDeleted(true);
        menuItemRepository.save(menuItem);
    }
    
    private MenuItemResponse mapToResponse(MenuItem menuItem) {
        return MenuItemResponse.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .price(menuItem.getPrice())
                .category(menuItem.getCategory())
                .status(menuItem.getStatus())
                .restaurantId(menuItem.getRestaurant().getId())
                .restaurantName(menuItem.getRestaurant().getName())
                .build();
    }
}

