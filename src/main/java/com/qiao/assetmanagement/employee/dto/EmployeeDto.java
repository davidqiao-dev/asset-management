package com.qiao.assetmanagement.employee.dto;

import com.qiao.assetmanagement.employee.Employee;

public record EmployeeDto(
        Long id,
        String name,
        String email,
        String department
) {
    public static EmployeeDto from(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment()
        );
    }
}
