package com.qiao.assetmanagement.equipment.dto;

import com.qiao.assetmanagement.equipment.Equipment;

public class EquipmentMapper {

    public static EquipmentDto toDto(Equipment equipment) {
        return new EquipmentDto(
                equipment.getId(),
                equipment.getAssetTag(),
                equipment.getType(),
                equipment.getBrand(),
                equipment.getStatus(),
                equipment.getPurchaseDate()
        );
    }

    public static Equipment toEntity(EquipmentRequest equipmentRequest) {
        Equipment equipment = new Equipment();
        equipment.setAssetTag(equipmentRequest.assetTag());
        equipment.setType(equipmentRequest.type());
        equipment.setBrand(equipmentRequest.brand());
        equipment.setPurchaseDate(equipmentRequest.purchaseDate());
        return equipment;
    }

    public static void setEntity(EquipmentRequest equipmentRequest, Equipment equipment) {
        equipment.setAssetTag(equipmentRequest.assetTag());
        equipment.setType(equipmentRequest.type());
        equipment.setBrand(equipmentRequest.brand());
        equipment.setPurchaseDate(equipmentRequest.purchaseDate());
    }
}
