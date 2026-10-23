package com.example.employeeintegration.service;

public class DuplicateEmployeeException extends RuntimeException {
    public DuplicateEmployeeException(String employeeId) {
        super("Employee already exists: " + employeeId);
    }
}
