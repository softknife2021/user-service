package com.softknife.userservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Simulates different runtime error conditions for log correlation testing.
 * Each method produces a real exception with a stack trace that points
 * to this class + line number — enabling Code-Test-Log Triangle correlation.
 */
@Service
@Slf4j
public class ErrorSimulatorService {

    /**
     * Throws NullPointerException — simulates accessing uninitialized data.
     */
    public String processNullValue() {
        log.debug("Processing value lookup...");
        String data = fetchFromCache("user-profile-123");
        // NPE here — data is null
        return data.toUpperCase();
    }

    /**
     * Throws StackOverflowError — simulates infinite recursion bug.
     */
    public int computeRecursive(int depth) {
        // Missing base case — infinite recursion
        return computeRecursive(depth + 1);
    }

    /**
     * Logs OutOfMemoryError and GC pressure — simulates memory leak.
     */
    public void simulateMemoryPressure() {
        log.error("java.lang.OutOfMemoryError: Java heap space");
        log.error("\tat com.softknife.userservice.service.ErrorSimulatorService.simulateMemoryPressure(ErrorSimulatorService.java:38)");
        log.error("\tat com.softknife.userservice.controller.ErrorSimulatorController.outOfMemory(ErrorSimulatorController.java:42)");
        log.error("GC overhead limit exceeded — full GC took 8.2s, freed only 2% of heap");
    }

    /**
     * Logs slow query warnings — simulates DB performance degradation.
     */
    public void simulateSlowQuery() {
        log.warn("Slow query detected: SELECT * FROM users WHERE email LIKE '%test%' execution time: 4500ms");
        log.warn("HikariPool-1 - Connection is not available, request timed out after 30000ms");
        log.warn("\tat com.softknife.userservice.service.UserService.findByEmail(UserService.java:45)");
        log.warn("\tat com.softknife.userservice.controller.UserController.listUsers(UserController.java:24)");
    }

    /**
     * Logs connection pool exhaustion — simulates resource leak.
     */
    public void simulateConnectionExhaustion() {
        log.error("HikariPool-1 - Connection pool exhausted: no available connections (maxPoolSize=10, activeConnections=10)");
        log.error("\tat com.softknife.userservice.service.UserService.findAll(UserService.java:30)");
        log.error("\tat com.softknife.userservice.controller.UserController.listUsers(UserController.java:25)");
        log.error("Connection refused: too many connections — service degraded");
        log.warn("Retry attempt 3 of 5 for database connection");
        log.warn("Circuit breaker OPEN for database pool — fallback activated");
    }

    /**
     * Logs security warnings — simulates auth/crypto issues.
     */
    public void simulateSecurityIssue() {
        log.error("Invalid JWT token: signature verification failed");
        log.error("\tat com.softknife.userservice.config.JwtAuthFilter.doFilterInternal(JwtAuthFilter.java:32)");
        log.warn("Unauthorized access attempt to /api/admin/users from IP 10.0.2.15");
        log.warn("Insecure cipher suite detected: TLS_RSA_WITH_AES_128_CBC_SHA — weak cipher");
    }

    /**
     * Logs deadlock — simulates concurrent DB access issues.
     */
    public void simulateDeadlock() {
        log.error("Deadlock detected: transaction 1 waiting for lock held by transaction 2");
        log.error("\tat com.softknife.userservice.service.UserService.updateUser(UserService.java:62)");
        log.error("\tat com.softknife.userservice.controller.UserController.updateUser(UserController.java:63)");
        log.error("Lock wait timeout exceeded — rolling back transaction after 30s");
        log.error("Thread starvation detected in pool 'db-pool': 10/10 threads blocked for >60s");
    }

    /**
     * Logs deprecation warnings — simulates framework upgrade issues.
     */
    public void simulateDeprecation() {
        log.warn("@Deprecated: Method UserService.findByName() will be removed in v3.0. Use findByUsername() instead");
        log.warn("Spring Security: authorizeRequests() is deprecated — use authorizeHttpRequests()");
        log.warn("DeprecationWarning: BCrypt rounds=4 is below minimum. Upgrade to rounds=12");
    }

    /**
     * Throws IllegalStateException after logging — simulates mixed failure.
     */
    public String processWithMultipleErrors() {
        log.warn("Slow query detected: SELECT u.* FROM users u JOIN roles r took 3200ms");
        log.error("Connection refused: SMTP server not responding — email notification failed for user signup");
        log.error("\tat com.softknife.userservice.service.UserService.sendNotification(UserService.java:87)");
        log.error("\tat com.softknife.userservice.controller.UserController.createUser(UserController.java:50)");
        log.warn("Retry attempt 2 of 3 for email delivery");
        throw new IllegalStateException("Multiple subsystem failures: DB slow + SMTP down");
    }

    private String fetchFromCache(String key) {
        // Simulates cache miss returning null
        return null;
    }
}
