package com.qiao.assetmanagement.equipment.dto;

import com.qiao.assetmanagement.equipment.EquipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EquipmentRequest(
        @NotBlank(message = "asset tag must not be blank")
        @Size(max = 50, message = "assetTag must be at most 50 characters")
        String assetTag,
        @NotBlank(message = "type must not be blank")
        @Size(max = 50, message = "type must be at most 50 characters")
        String type,
        @Size(max = 100, message = "brand must be at most 100 characters")
        String brand,
        @PastOrPresent(message = "purchase date cannot be in the future")
        LocalDate purchaseDate
) {
}
