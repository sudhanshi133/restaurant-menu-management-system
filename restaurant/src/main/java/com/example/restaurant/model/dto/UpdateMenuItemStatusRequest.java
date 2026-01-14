package com.example.restaurant.model.dto;

import com.example.restaurant.model.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMenuItemStatusRequest {
    
    @NotNull(message = "Status is required")
    private Status status;
}

