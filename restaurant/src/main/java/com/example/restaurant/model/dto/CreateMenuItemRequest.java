package com.example.restaurant.model.dto;

import com.example.restaurant.model.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuItemRequest {
    
    @NotBlank(message = "Menu item name is mandatory")
    private String name;
    
    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be greater than 0")
    private Double price;
    
    @NotNull(message = "Category is mandatory")
    private Category category;
}

