package com.qiao.assetmanagement;

import com.qiao.assetmanagement.assignment.AssignmentRepository;
import com.qiao.assetmanagement.common.exception.BusinessRuleException;
import com.qiao.assetmanagement.employee.EmployeeService;
import com.qiao.assetmanagement.employee.dto.EmployeeRequest;
import com.qiao.assetmanagement.equipment.EquipmentService;
import com.qiao.assetmanagement.equipment.EquipmentStatus;
import com.qiao.assetmanagement.equipment.dto.EquipmentRequest;
import com.qiao.assetmanagement.equipment.dto.EquipmentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AssetManagementApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EquipmentService equipmentService;

    @Test
    void appStartsAndReadsSeedRow() {
        long count = assignmentRepository.count();
        System.out.println("Number of assignment records: " + count);
        assertEquals(1, count);

    }

    @Test
    void shouldRejectDuplicateEmail() {
        EmployeeRequest a =
                new EmployeeRequest("A", "dup@example.com", "IT");
        EmployeeRequest b =
                new EmployeeRequest("B", "dup@example.com", "IT");
        employeeService.create(a);
        assertThatThrownBy(() -> employeeService.create(b))
                .isInstanceOf(BusinessRuleException.class);

    }

    @Test
    void newEquipmentStartAvailable() {
        EquipmentResponse equipmentResponse = equipmentService.create(
                new EquipmentRequest(
                        "TAG-TEST", "LAPTOP", null, null)
        );

        assertThat(equipmentResponse.status()).isEqualTo(EquipmentStatus.AVAILABLE);
    }

}
