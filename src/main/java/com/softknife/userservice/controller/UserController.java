package com.softknife.userservice.controller;

import com.softknife.userservice.model.User;
import com.softknife.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> listUsers() {
        List<Map<String, Object>> data = userService.findAll().stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", data);
        response.put("total", data.size());
        response.put("page", 1);
        response.put("pageSize", 20);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(u -> ResponseEntity.ok((Object) toDetailResponse(u)))
                .orElse(ResponseEntity.status(404).body(Map.of(
                        "error", "NOT_FOUND",
                        "message", "User with id " + id + " not found",
                        "code", "USER_001",
                        "timestamp", Instant.now().toString()
                )));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User created = userService.create(user);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", created.getId());
        response.put("username", created.getUsername());
        response.put("email", created.getEmail());
        response.put("role", created.getRole());
        response.put("active", created.isActive());
        response.put("createdAt", Instant.now().toString());
        response.put("message", "User created successfully");
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user)
                .map(u -> {
                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("id", u.getId());
                    response.put("username", u.getUsername());
                    response.put("email", u.getEmail());
                    response.put("role", u.getRole());
                    response.put("active", u.isActive());
                    response.put("updatedAt", Instant.now().toString());
                    response.put("message", "User updated successfully");
                    return ResponseEntity.ok((Object) response);
                })
                .orElse(ResponseEntity.status(404).body(Map.of(
                        "error", "NOT_FOUND",
                        "message", "User with id " + id + " not found"
                )));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.delete(id)) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("id", id);
            response.put("deleted", true);
            response.put("message", "User deleted successfully");
            response.put("deletedAt", Instant.now().toString());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(404).body(Map.of(
                "error", "NOT_FOUND",
                "message", "User with id " + id + " not found"
        ));
    }

    private Map<String, Object> toListResponse(User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("role", user.getRole());
        map.put("active", user.isActive());
        map.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        return map;
    }

    private Map<String, Object> toDetailResponse(User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("role", user.getRole());
        map.put("active", user.isActive());

        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("firstName", "User");
        profile.put("lastName", String.valueOf(user.getId()));
        profile.put("department", "Engineering");
        profile.put("phone", "+1-555-010" + user.getId());
        map.put("profile", profile);

        map.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        map.put("updatedAt", user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null);
        return map;
    }
}
