package com.example.restaurant.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRestaurantRequest {
    
    @NotBlank(message = "Restaurant name is mandatory")
    private String name;
    
    private String location;
}

