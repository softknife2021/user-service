package com.softknife.userservice.controller;

import com.softknife.userservice.model.User;
import com.softknife.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(Map.of(
                "data", data,
                "total", data.size(),
                "page", 1,
                "pageSize", 20
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(u -> ResponseEntity.ok(toResponse(u)))
                .orElse(ResponseEntity.status(404).body(Map.of(
                        "error", "User not found",
                        "message", "No user with id " + id
                )));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User created = userService.create(user);
        return ResponseEntity.status(201).body(toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user)
                .map(u -> ResponseEntity.ok(toResponse(u)))
                .orElse(ResponseEntity.status(404).body(Map.of(
                        "error", "User not found"
                )));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.delete(id)) {
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        }
        return ResponseEntity.status(404).body(Map.of("error", "User not found"));
    }

    private Map<String, Object> toResponse(User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("role", user.getRole());
        map.put("active", user.isActive());
        map.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        return map;
    }
}
