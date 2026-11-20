package com.qiao.assetmanagement.assignment.dto;

import java.time.LocalDate;

public record AssignmentResponse(
        Long id,
        Long employeeId,
        String employeeName,
        Long equipmentId,
        String assetTag,
        LocalDate assignedDate,
        LocalDate returnedDate
) {
}
