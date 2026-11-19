package com.qiao.assetmanagement.employee.dto;

import com.qiao.assetmanagement.employee.Employee;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
        String department
) {
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment()
        );
    }
}
