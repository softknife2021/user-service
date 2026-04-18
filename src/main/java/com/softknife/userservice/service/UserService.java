package com.softknife.userservice.service;

import com.softknife.userservice.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(4);

    public UserService() {
        // Seed data matching WireMock stubs
        users.put(1L, User.builder().id(1L).username("admin").email("admin@example.com")
                .password("admin123").role("ADMIN").active(true)
                .createdAt(LocalDateTime.of(2024, 1, 15, 10, 30)).build());
        users.put(2L, User.builder().id(2L).username("john.doe").email("john.doe@example.com")
                .password("password123").role("USER").active(true)
                .createdAt(LocalDateTime.of(2024, 2, 20, 14, 45)).build());
        users.put(3L, User.builder().id(3L).username("jane.smith").email("jane.smith@example.com")
                .password("password123").role("USER").active(false)
                .createdAt(LocalDateTime.of(2024, 3, 10, 9, 15)).build());
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public User create(User user) {
        user.setId(idCounter.getAndIncrement());
        user.setCreatedAt(LocalDateTime.now());
        if (user.getRole() == null) user.setRole("USER");
        user.setActive(true);
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> update(Long id, User updated) {
        User existing = users.get(id);
        if (existing == null) return Optional.empty();

        if (updated.getUsername() != null) existing.setUsername(updated.getUsername());
        if (updated.getEmail() != null) existing.setEmail(updated.getEmail());
        if (updated.getRole() != null) existing.setRole(updated.getRole());
        existing.setActive(updated.isActive());

        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return users.remove(id) != null;
    }
}
