package com.qiao.assetmanagement.assignment.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AssignRequest(
        @NotNull(message = "employeeId must not be null")
        Long employeeId,
        @NotNull(message = "equipmentId must not be null")
        Long equipmentId,
        LocalDate assignedDate,
        LocalDate returnedDate
) {
}
