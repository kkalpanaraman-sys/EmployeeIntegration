package com.example.employeeintegration.api;

import com.example.employeeintegration.service.DuplicateEmployeeException;
import com.example.employeeintegration.service.EmployeeNotFoundException;
import org.apache.camel.CamelExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CamelExecutionException.class)
    public ResponseEntity<Map<String, String>> handleCamel(CamelExecutionException exception) {
        Throwable cause = rootCause(exception);

        if (cause instanceof EmployeeNotFoundException) {
            return error(HttpStatus.NOT_FOUND, cause.getMessage());
        }
        if (cause instanceof DuplicateEmployeeException) {
            return error(HttpStatus.CONFLICT, cause.getMessage());
        }
        if (cause instanceof IllegalArgumentException) {
            return error(HttpStatus.BAD_REQUEST, cause.getMessage());
        }

        return error(HttpStatus.INTERNAL_SERVER_ERROR, "The integration request could not be processed");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Request validation failed");
        return error(HttpStatus.BAD_REQUEST, message);
    }

    private Throwable rootCause(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        // Some wrappers contain the domain exception one level above the root cause.
        Throwable cursor = throwable;
        while (cursor != null) {
            if (cursor instanceof EmployeeNotFoundException
                    || cursor instanceof DuplicateEmployeeException
                    || cursor instanceof IllegalArgumentException) {
                return cursor;
            }
            cursor = cursor.getCause();
        }
        return current;
    }

    private ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("status", status.name());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
