package com.example.employeeintegration;

import com.example.employeeintegration.api.EmployeeRequest;
import com.example.employeeintegration.api.EmployeeResponse;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmployeeIntegrationApplicationTests {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Test
    void createsAndRetrievesEmployeeThroughCamelRoutes() {
        EmployeeRequest request = new EmployeeRequest(
                "TEST-1001",
                "Demo Employee",
                "demo.employee@example.com",
                "Engineering",
                "ACTIVE"
        );

        EmployeeResponse created = producerTemplate.requestBody(
                "direct:employee-create", request, EmployeeResponse.class);

        EmployeeResponse retrieved = producerTemplate.requestBodyAndHeader(
                "direct:employee-get", null, "employeeId", "TEST-1001", EmployeeResponse.class);

        assertThat(created.employeeId()).isEqualTo("TEST-1001");
        assertThat(retrieved.email()).isEqualTo("demo.employee@example.com");
    }
}
