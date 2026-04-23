package com.softknife.userservice.controller;

import com.softknife.userservice.service.ErrorSimulatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Error simulation endpoints for testing the Code-Test-Log Triangle.
 * Each endpoint calls a service method that produces real exceptions
 * with stack traces pointing to specific Java classes and line numbers.
 */
@RestController
@RequestMapping("/api/test/errors")
@RequiredArgsConstructor
@Slf4j
public class ErrorSimulatorController {

    private final ErrorSimulatorService errorService;

    @GetMapping("/null-pointer")
    public ResponseEntity<?> nullPointer() {
        String result = errorService.processNullValue();
        return ResponseEntity.ok(Map.of("result", result));
    }

    @GetMapping("/stack-overflow")
    public ResponseEntity<?> stackoverflow() {
        int result = errorService.computeRecursive(0);
        return ResponseEntity.ok(Map.of("result", result));
    }

    @GetMapping("/out-of-memory")
    public ResponseEntity<?> outOfMemory() {
        errorService.simulateMemoryPressure();
        return ResponseEntity.ok(Map.of("status", "memory_pressure_logged"));
    }

    @GetMapping("/slow-query")
    public ResponseEntity<?> slowQuery() {
        errorService.simulateSlowQuery();
        return ResponseEntity.ok(Map.of("status", "slow_query_logged"));
    }

    @GetMapping("/connection-pool")
    public ResponseEntity<?> connectionPool() {
        errorService.simulateConnectionExhaustion();
        return ResponseEntity.status(503).body(Map.of(
                "error", "SERVICE_UNAVAILABLE",
                "message", "Connection pool exhausted"));
    }

    @GetMapping("/security")
    public ResponseEntity<?> securityWarning() {
        errorService.simulateSecurityIssue();
        return ResponseEntity.status(401).body(Map.of(
                "error", "UNAUTHORIZED",
                "message", "Security validation failed"));
    }

    @GetMapping("/deadlock")
    public ResponseEntity<?> deadlock() {
        errorService.simulateDeadlock();
        return ResponseEntity.status(500).body(Map.of(
                "error", "DEADLOCK",
                "message", "Database deadlock detected"));
    }

    @GetMapping("/deprecation")
    public ResponseEntity<?> deprecation() {
        errorService.simulateDeprecation();
        return ResponseEntity.ok(Map.of("status", "deprecation_logged"));
    }

    @GetMapping("/mixed")
    public ResponseEntity<?> mixedErrors() {
        String result = errorService.processWithMultipleErrors();
        return ResponseEntity.ok(Map.of("result", result));
    }
}
