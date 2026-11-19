package com.qiao.assetmanagement.equipment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByAssetTag(String assetTag);

    Page<Equipment> findByStatus(EquipmentStatus status, Pageable pageable);

    boolean existsByAssetTag(String assetTag);
}
