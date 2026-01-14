package com.example.restaurant.model.dto;

import com.example.restaurant.model.enums.Category;
import com.example.restaurant.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemResponse {
    
    private Long id;
    private String name;
    private Double price;
    private Category category;
    private Status status;
    private Long restaurantId;
    private String restaurantName;
}

