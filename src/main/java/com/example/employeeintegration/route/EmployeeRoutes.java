package com.example.employeeintegration.route;

import com.example.employeeintegration.api.EmployeeRequest;
import com.example.employeeintegration.api.EmployeeResponse;
import com.example.employeeintegration.service.EmployeeService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Camel routes provide a separate integration layer between the REST API
 * and the employee persistence service.
 */
@Component
public class EmployeeRoutes extends RouteBuilder {
    private final EmployeeService employeeService;

    public EmployeeRoutes(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void configure() {
        from("direct:employee-create")
                .routeId("employee-create-route")
                .log("Processing employee create request")
                .process(exchange -> {
                    EmployeeRequest request = exchange.getMessage().getBody(EmployeeRequest.class);
                    EmployeeResponse response = employeeService.create(request);
                    exchange.getMessage().setBody(response);
                });

        from("direct:employee-update")
                .routeId("employee-update-route")
                .log("Processing employee update request for ${header.employeeId}")
                .process(exchange -> {
                    String employeeId = exchange.getMessage().getHeader("employeeId", String.class);
                    EmployeeRequest request = exchange.getMessage().getBody(EmployeeRequest.class);
                    exchange.getMessage().setBody(employeeService.update(employeeId, request));
                });

        from("direct:employee-get")
                .routeId("employee-get-route")
                .process(exchange -> {
                    String employeeId = exchange.getMessage().getHeader("employeeId", String.class);
                    exchange.getMessage().setBody(employeeService.getByEmployeeId(employeeId));
                });

        from("direct:employee-list")
                .routeId("employee-list-route")
                .process(exchange -> exchange.getMessage().setBody(employeeService.getAll()));

        from("direct:employee-delete")
                .routeId("employee-delete-route")
                .log("Processing employee delete request for ${header.employeeId}")
                .process(exchange -> {
                    String employeeId = exchange.getMessage().getHeader("employeeId", String.class);
                    employeeService.delete(employeeId);
                    exchange.getMessage().setBody(null);
                });
    }
}
