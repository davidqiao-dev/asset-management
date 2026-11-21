package com.qiao.assetmanagement.equipment;

import com.qiao.assetmanagement.equipment.projection.StatusCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByAssetTag(String assetTag);

    Page<Equipment> findByStatus(EquipmentStatus status, Pageable pageable);

    boolean existsByAssetTag(String assetTag);

    @Query("select e.status as status, count(e) as count from Equipment e group by e.status")
    List<StatusCount> countByStatus();
}
