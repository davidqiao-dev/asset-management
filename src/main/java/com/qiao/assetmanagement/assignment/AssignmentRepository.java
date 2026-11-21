package com.qiao.assetmanagement.assignment;

import com.qiao.assetmanagement.assignment.projection.EmployeeAssignmentCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByEquipmentIdAndReturnedDateIsNull(Long equipmentId);

    List<Assignment> findByEmployeeIdAndReturnedDateIsNull(Long employeeId);

    Page<Assignment> findByReturnedDateIsNull(Pageable pageable);

    @Query("""
            select e.name as employeeName, count(a) as assignmentCount
                        from Assignment a
                        join a.employee e
                        where a.returnedDate is null
                        group by e.id, e.name
                        order by count(a) desc
            """)
    List<EmployeeAssignmentCount> countActiveAssignmentCountByEmployee();
}
