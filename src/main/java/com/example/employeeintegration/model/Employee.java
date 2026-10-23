package com.example.employeeintegration.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employees", uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_business_id", columnNames = "employee_id")
})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, unique = true, length = 64)
    private String employeeId;

    @Column(name = "full_name", nullable = false, length = 160)
    private String fullName;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(length = 100)
    private String department;

    @Column(name = "employment_status", nullable = false, length = 32)
    private String employmentStatus;

    protected Employee() {
        // Required by JPA
    }

    public Employee(String employeeId, String fullName, String email,
                    String department, String employmentStatus) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.employmentStatus = employmentStatus;
    }

    public Long getId() { return id; }
    public String getEmployeeId() { return employeeId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public String getEmploymentStatus() { return employmentStatus; }

    public void updateFrom(String fullName, String email, String department, String employmentStatus) {
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.employmentStatus = employmentStatus;
    }
}
