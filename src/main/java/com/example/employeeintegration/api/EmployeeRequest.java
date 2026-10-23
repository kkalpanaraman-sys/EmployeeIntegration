package com.example.employeeintegration.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(
        @NotBlank @Size(max = 64) String employeeId,
        @NotBlank @Size(max = 160) String fullName,
        @NotBlank @Email @Size(max = 254) String email,
        @Size(max = 100) String department,
        @NotBlank @Size(max = 32) String employmentStatus
) {}
