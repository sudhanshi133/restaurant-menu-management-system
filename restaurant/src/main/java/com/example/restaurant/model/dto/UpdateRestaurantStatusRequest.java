package com.example.restaurant.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRestaurantStatusRequest {
    
    @NotNull(message = "isOpen status is required")
    private Boolean isOpen;
}

