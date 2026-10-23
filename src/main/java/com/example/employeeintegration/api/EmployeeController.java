package com.example.employeeintegration.api;

import jakarta.validation.Valid;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final ProducerTemplate producerTemplate;

    public EmployeeController(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = producerTemplate.requestBody(
                "direct:employee-create", request, EmployeeResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{employeeId}")
    public EmployeeResponse update(
            @PathVariable String employeeId,
            @Valid @RequestBody EmployeeRequest request) {
        return producerTemplate.requestBodyAndHeader(
                "direct:employee-update", request, "employeeId", employeeId, EmployeeResponse.class);
    }

    @GetMapping("/{employeeId}")
    public EmployeeResponse getById(@PathVariable String employeeId) {
        return producerTemplate.requestBodyAndHeader(
                "direct:employee-get", null, "employeeId", employeeId, EmployeeResponse.class);
    }

    @GetMapping
    public List<EmployeeResponse> getAll() {
        return producerTemplate.requestBody("direct:employee-list", null, List.class);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> delete(@PathVariable String employeeId) {
        producerTemplate.requestBodyAndHeader(
                "direct:employee-delete", null, "employeeId", employeeId, Object.class);
        return ResponseEntity.noContent().build();
    }
}
