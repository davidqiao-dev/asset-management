package com.qiao.assetmanagement.assignment;

import com.qiao.assetmanagement.assignment.dto.AssignmentMapper;
import com.qiao.assetmanagement.assignment.dto.AssignmentRequest;
import com.qiao.assetmanagement.assignment.dto.AssignmentResponse;
import com.qiao.assetmanagement.common.exception.BusinessRuleException;
import com.qiao.assetmanagement.common.exception.ResourceNotFoundException;
import com.qiao.assetmanagement.employee.Employee;
import com.qiao.assetmanagement.employee.EmployeeRepository;
import com.qiao.assetmanagement.equipment.Equipment;
import com.qiao.assetmanagement.equipment.EquipmentRepository;
import com.qiao.assetmanagement.equipment.EquipmentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class AssignmentService {

    private EmployeeRepository employeeRepository;
    private EquipmentRepository equipmentRepository;
    private AssignmentRepository assignmentRepository;

    public AssignmentService(EmployeeRepository employeeRepository,
                             EquipmentRepository equipmentRepository,
                             AssignmentRepository assignmentRepository) {

        this.employeeRepository = employeeRepository;
        this.equipmentRepository = equipmentRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Transactional
    public AssignmentResponse assign(Long employeeId, Long equipmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", employeeId));

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", equipmentId));

        if (equipment.getStatus() == EquipmentStatus.RETIRED) {
            throw new BusinessRuleException(
                    "Equipment " + equipment.getAssetTag()
                            + " is retired and cannot be assigned.");
        }

        if (equipment.getStatus() != EquipmentStatus.AVAILABLE) {
            throw new BusinessRuleException("Equipment "
                    + equipment.getAssetTag() + " is not available.");
        }

        if (assignmentRepository.findByEquipmentIdAndReturnedDateIsNull(
                equipmentId).isPresent()) {
            throw new BusinessRuleException("Equipment "
                    + equipment.getAssetTag()
                    + " already has an active assignment.");
        }

        Assignment assignment = new Assignment();
        assignment.setEquipment(equipment);
        assignment.setEmployee(employee);
        assignment.setAssignedDate(LocalDate.now());

        equipment.setStatus(EquipmentStatus.ASSIGNED);
        equipmentRepository.save(equipment);

        return AssignmentMapper.toResponse(assignmentRepository.save(assignment));

    }

    @Transactional
    public AssignmentResponse returnAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment", id));
        if (assignment.getReturnedDate() != null) {
            throw new BusinessRuleException("Assignment "
                    + id + " is already returned.");
        }

        assignment.setReturnedDate(LocalDate.now());

        Equipment equipment = assignment.getEquipment();
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        equipmentRepository.save(equipment);

        return AssignmentMapper.toResponse(assignmentRepository.save(assignment));
    }
}
