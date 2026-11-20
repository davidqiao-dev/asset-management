package com.qiao.assetmanagement;

import com.qiao.assetmanagement.assignment.AssignmentService;
import com.qiao.assetmanagement.common.exception.ResourceNotFoundException;
import com.qiao.assetmanagement.employee.EmployeeRepository;
import com.qiao.assetmanagement.equipment.EquipmentRepository;
import com.qiao.assetmanagement.equipment.EquipmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest()
public class AssignmentServiceTest {

    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void assigningUnavailableEquipmentLeavesStatusUntouched() {

        // equipment 1 in seed is ASSIGNED already; pick a fresh available one
        var available = equipmentRepository.findByStatus(EquipmentStatus.AVAILABLE
                , PageRequest.of(0, 1)).getContent();

        assertThat(available).isNotEmpty();

        var eq = available.get(0);
        Long originalStatus = null; // just assert after

        assertThatThrownBy(() -> assignmentService.assign(99999L, eq.getId()))
                .isInstanceOf(ResourceNotFoundException.class);

        // employee didn't exist -> nothing should have changed
        var reloaded = equipmentRepository.findById(eq.getId()).orElseThrow();

        assertThat(reloaded.getStatus()).isEqualTo(EquipmentStatus.AVAILABLE);

    }

    @Test
    @Transactional
    void assignThenReturnRestoreAvailability() {

        // assign available equipment to existing employee
        var dto = assignmentService.assign(1L, 2L);
        assertThat(dto.returnedDate()).isNull();

        var eq = equipmentRepository.findById(dto.equipmentId()).orElseThrow();
        assertThat(eq.getStatus()).isEqualTo(EquipmentStatus.ASSIGNED);

        var returned = assignmentService.returnAssignment(dto.id());
        assertThat(returned.returnedDate()).isNotNull();

        var eqAfter = equipmentRepository.findById(dto.equipmentId()).orElseThrow();
        assertThat(eqAfter.getStatus()).isEqualTo(EquipmentStatus.AVAILABLE);
    }
}
