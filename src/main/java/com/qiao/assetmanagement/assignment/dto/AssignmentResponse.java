package com.qiao.assetmanagement.assignment.dto;

import java.time.LocalDate;

public record AssignmentResponse(
        Long id,
        Long employeeId,
        Long equipmentId,
        LocalDate assignedDate,
        LocalDate returnedDate
) {
}
