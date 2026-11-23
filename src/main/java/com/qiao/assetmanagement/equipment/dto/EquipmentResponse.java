package com.qiao.assetmanagement.equipment.dto;

import com.qiao.assetmanagement.equipment.EquipmentStatus;

import java.time.LocalDate;

public record EquipmentResponse(
        Long id,
        String assetTag,
        String type,
        String brand,
        EquipmentStatus status,
        LocalDate purchaseDate
) {

}
