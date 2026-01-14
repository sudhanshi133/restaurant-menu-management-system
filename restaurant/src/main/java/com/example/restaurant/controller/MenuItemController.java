package com.example.restaurant.controller;

import com.example.restaurant.model.dto.CreateMenuItemRequest;
import com.example.restaurant.model.dto.MenuItemResponse;
import com.example.restaurant.model.dto.UpdateMenuItemStatusRequest;
import com.example.restaurant.model.enums.Category;
import com.example.restaurant.model.enums.Status;
import com.example.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MenuItemController {
    
    private final MenuItemService menuItemService;
    
    @PostMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<MenuItemResponse> addMenuItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateMenuItemRequest request) {
        MenuItemResponse response = menuItemService.addMenuItem(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<Page<MenuItemResponse>> getMenuItems(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        

        Page<MenuItemResponse> response = menuItemService.getMenuItems(
            restaurantId, category, status);
        
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/menu-items/{menuItemId}/status")
    public ResponseEntity<MenuItemResponse> updateMenuItemStatus(
            @PathVariable Long menuItemId,
            @Valid @RequestBody UpdateMenuItemStatusRequest request) {
        MenuItemResponse response = menuItemService.updateMenuItemStatus(menuItemId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/menu-items/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long menuItemId) {
        menuItemService.deleteMenuItem(menuItemId);
        return ResponseEntity.noContent().build();
    }
}

