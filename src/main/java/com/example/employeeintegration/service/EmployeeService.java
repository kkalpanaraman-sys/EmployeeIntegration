package com.example.employeeintegration.service;

import com.example.employeeintegration.api.EmployeeRequest;
import com.example.employeeintegration.api.EmployeeResponse;
import com.example.employeeintegration.model.Employee;
import com.example.employeeintegration.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public EmployeeResponse create(EmployeeRequest request) {
        if (repository.existsByEmployeeId(request.employeeId())) {
            throw new DuplicateEmployeeException(request.employeeId());
        }

        Employee employee = new Employee(
                request.employeeId(),
                request.fullName(),
                request.email(),
                request.department(),
                request.employmentStatus()
        );
        return EmployeeResponse.from(repository.save(employee));
    }

    public EmployeeResponse update(String employeeId, EmployeeRequest request) {
        Employee employee = repository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        // The path ID is the stable key; don't silently change it through the request body.
        if (!employeeId.equals(request.employeeId())) {
            throw new IllegalArgumentException(
                    "employeeId in the request body must match the employeeId in the URL");
        }

        employee.updateFrom(
                request.fullName(),
                request.email(),
                request.department(),
                request.employmentStatus()
        );
        return EmployeeResponse.from(repository.save(employee));
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getByEmployeeId(String employeeId) {
        return repository.findByEmployeeId(employeeId)
                .map(EmployeeResponse::from)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAll() {
        return repository.findAll().stream().map(EmployeeResponse::from).toList();
    }

    public void delete(String employeeId) {
        Employee employee = repository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        repository.delete(employee);
    }
}
