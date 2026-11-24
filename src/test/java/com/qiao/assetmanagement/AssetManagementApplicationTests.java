package com.qiao.assetmanagement;

import com.qiao.assetmanagement.assignment.AssignmentRepository;
import com.qiao.assetmanagement.common.exception.BusinessRuleException;
import com.qiao.assetmanagement.employee.EmployeeRepository;
import com.qiao.assetmanagement.employee.EmployeeService;
import com.qiao.assetmanagement.employee.dto.EmployeeRequest;
import com.qiao.assetmanagement.equipment.EquipmentRepository;
import com.qiao.assetmanagement.equipment.EquipmentService;
import com.qiao.assetmanagement.equipment.EquipmentStatus;
import com.qiao.assetmanagement.equipment.dto.EquipmentRequest;
import com.qiao.assetmanagement.equipment.dto.EquipmentResponse;
import com.qiao.assetmanagement.equipment.projection.StatusCount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AssetManagementApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EquipmentService equipmentService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    void shouldRejectDuplicateEmail() {
        String email = "u-" + UUID.randomUUID() + "@example.com";
        EmployeeRequest a =
                new EmployeeRequest("A", email, "IT");
        EmployeeRequest b =
                new EmployeeRequest("B", email, "IT");
        employeeService.create(a);
        assertThatThrownBy(() -> employeeService.create(b))
                .isInstanceOf(BusinessRuleException.class);

    }


    @Test
    void countByStatusGroupsEveryStatus() {
        var counts = equipmentRepository.countByStatus();
        assertThat(counts).isNotEmpty();

        // every status present should sum to total equipment
        long total = counts.stream().mapToLong(StatusCount::getCount).sum();
        assertThat(total).isEqualTo(equipmentRepository.count());
    }

    @Test
    void activeByEmployeeReturnsLongCounts() {
        var report = assignmentRepository.countActiveAssignmentCountByEmployee();
        assertThat(report).allSatisfy(
                r -> assertThat(r.getAssignmentCount()).isNotNull());
    }
}
