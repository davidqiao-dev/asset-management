package com.qiao.assetmanagement.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(
        @NotBlank(message = "name must not be blank")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,
        @NotBlank(message = "email must not be blank")
        @Email(message = "email is invalid")
        @Size(max = 150, message = "email must be at most 150 characters")
        String email,
        @Size(max = 50, message = "department must be at most 150 characters")
        String department
) {
}
