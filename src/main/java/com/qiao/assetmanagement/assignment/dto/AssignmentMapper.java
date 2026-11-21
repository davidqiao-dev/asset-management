package com.qiao.assetmanagement.assignment.dto;

import com.qiao.assetmanagement.assignment.Assignment;

public class AssignmentMapper {
    public static AssignmentDto toDto(Assignment assignment) {
        return new AssignmentDto(
                assignment.getId(),
                assignment.getEmployee().getId(),
                assignment.getEmployee().getName(),
                assignment.getEquipment().getId(),
                assignment.getEquipment().getAssetTag(),
                assignment.getAssignedDate(),
                assignment.getReturnedDate()
        );
    }

    public static Assignment toEntity(AssignRequest assignRequest) {
        Assignment assignment = new Assignment();
        assignment.setAssignedDate(assignRequest.assignedDate());
        assignment.setReturnedDate(assignRequest.returnedDate());
        return assignment;
    }
}
