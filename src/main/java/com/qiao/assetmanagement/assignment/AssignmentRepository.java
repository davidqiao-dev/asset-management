package com.qiao.assetmanagement.assignment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByEquipmentIdAndReturnedDateIsNull(Long equipmentId);
    List<Assignment> findByEmployeeIdAndReturnedDateIsNull(Long employeeId);
    Page<Assignment> findByReturnedDateIsNull(Pageable pageable);

}
