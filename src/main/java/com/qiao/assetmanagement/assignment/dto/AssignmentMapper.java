package com.qiao.assetmanagement.assignment.dto;

import com.qiao.assetmanagement.assignment.Assignment;

public class AssignmentMapper {
    public static AssignmentResponse toResponse(Assignment assignment) {
        return new AssignmentResponse(
                assignment.getId(),
                assignment.getEmployee().getId(),
                assignment.getEquipment().getId(),
                assignment.getAssignedDate(),
                assignment.getReturnedDate()
        );
    }

    public static Assignment toEntity(AssignmentRequest assignmentRequest) {
        Assignment assignment = new Assignment();
        assignment.setAssignedDate(assignmentRequest.assignedDate());
        assignment.setReturnedDate(assignmentRequest.returnedDate());
        return assignment;
    }
}
