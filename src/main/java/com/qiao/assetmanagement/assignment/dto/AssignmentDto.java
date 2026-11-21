package com.qiao.assetmanagement.assignment.dto;

import java.time.LocalDate;

public record AssignmentDto(
        Long id,
        Long employeeId,
        String employeeName,
        Long equipmentId,
        String assetTag,
        LocalDate assignedDate,
        LocalDate returnedDate
) {
}
