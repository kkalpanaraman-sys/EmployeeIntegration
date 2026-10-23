package com.example.employeeintegration.service;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String employeeId) {
        super("Employee not found: " + employeeId);
    }
}
