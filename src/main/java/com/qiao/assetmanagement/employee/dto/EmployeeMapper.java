package com.qiao.assetmanagement.employee.dto;

import com.qiao.assetmanagement.employee.Employee;

public class EmployeeMapper {

    private EmployeeMapper() {
    }

    public static EmployeeDto toDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment()
        );
    }

    public static Employee toEntity(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setName(employeeRequest.name());
        employee.setEmail(employeeRequest.email());
        employee.setDepartment(employeeRequest.department());
        return employee;
    }

    public static void setEntity(EmployeeRequest employeeRequest, Employee employee) {
        employee.setName(employeeRequest.name());
        employee.setEmail(employeeRequest.email());
        employee.setDepartment(employeeRequest.department());
    }
}
