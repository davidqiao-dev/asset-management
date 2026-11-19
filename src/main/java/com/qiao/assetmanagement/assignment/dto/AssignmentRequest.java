package com.qiao.assetmanagement.assignment.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AssignmentRequest(
        @NotNull(message = "employeeId must not be null")
        Long employeeId,
        @NotNull(message = "equipmentId must not be null")
        Long equipmentId,
        @NotNull(message = "assigned date must not be null")
        LocalDate assignedDate,
        LocalDate returnedDate
) {
}
