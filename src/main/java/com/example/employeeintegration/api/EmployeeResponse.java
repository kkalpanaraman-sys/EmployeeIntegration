package com.example.employeeintegration.api;

import com.example.employeeintegration.model.Employee;

public record EmployeeResponse(
        Long id,
        String employeeId,
        String fullName,
        String email,
        String department,
        String employmentStatus
) {
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeId(),
                employee.getFullName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getEmploymentStatus()
        );
    }
}
