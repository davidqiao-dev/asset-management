package com.qiao.assetmanagement.assignment;

import com.qiao.assetmanagement.assignment.dto.AssignmentMapper;
import com.qiao.assetmanagement.assignment.dto.AssignmentDto;
import com.qiao.assetmanagement.assignment.projection.EmployeeAssignmentCount;
import com.qiao.assetmanagement.common.exception.BusinessRuleException;
import com.qiao.assetmanagement.common.exception.ResourceNotFoundException;
import com.qiao.assetmanagement.employee.Employee;
import com.qiao.assetmanagement.employee.EmployeeRepository;
import com.qiao.assetmanagement.equipment.Equipment;
import com.qiao.assetmanagement.equipment.EquipmentRepository;
import com.qiao.assetmanagement.equipment.EquipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssignmentService {

    private final EmployeeRepository employeeRepository;
    private final EquipmentRepository equipmentRepository;
    private final AssignmentRepository assignmentRepository;

    public AssignmentService(EmployeeRepository employeeRepository,
                             EquipmentRepository equipmentRepository,
                             AssignmentRepository assignmentRepository) {

        this.employeeRepository = employeeRepository;
        this.equipmentRepository = equipmentRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Transactional
    public AssignmentDto assign(Long employeeId, Long equipmentId) {
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

        return AssignmentMapper.toDto(assignmentRepository.save(assignment));

    }

    @Transactional
    public AssignmentDto returnAssignment(Long id) {
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

        return AssignmentMapper.toDto(assignmentRepository.save(assignment));
    }


    public Page<AssignmentDto> findActive(Pageable pageable) {
        return assignmentRepository.findByReturnedDateIsNull(pageable)
                .map(AssignmentMapper::toDto);
    }

    public List<AssignmentDto> currentAssignmentsForEmployee(Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", employeeId));

        return assignmentRepository.findByEmployeeIdAndReturnedDateIsNull(employeeId)
                .stream().map(AssignmentMapper::toDto).toList();
    }

    public List<EmployeeAssignmentCount> reportActiveByEmployee() {
        return assignmentRepository.countActiveAssignmentCountByEmployee();
    }
}
