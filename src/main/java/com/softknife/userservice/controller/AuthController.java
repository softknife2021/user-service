package com.softknife.userservice.controller;

import com.softknife.userservice.model.LoginRequest;
import com.softknife.userservice.service.JwtService;
import com.softknife.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.findByUsername(request.getUsername())
                .filter(u -> u.getPassword().equals(request.getPassword()))
                .map(u -> {
                    String token = jwtService.generateToken(u.getUsername(), u.getRole());
                    String refreshToken = "refresh-" + java.util.UUID.randomUUID();

                    Map<String, Object> user = new LinkedHashMap<>();
                    user.put("id", u.getId());
                    user.put("username", u.getUsername());
                    user.put("email", u.getEmail());
                    user.put("role", u.getRole());

                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("accessToken", token);
                    response.put("refreshToken", refreshToken);
                    response.put("tokenType", "Bearer");
                    response.put("expiresIn", 86400);
                    response.put("user", user);

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).body(Map.of(
                        "error", "INVALID_CREDENTIALS",
                        "message", "Invalid username or password",
                        "timestamp", Instant.now().toString()
                )));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "INVALID_REQUEST",
                    "message", "refreshToken is required"
            ));
        }
        // For demo: always return a new token
        String newToken = jwtService.generateToken("admin", "ADMIN");
        String newRefresh = "refresh-" + java.util.UUID.randomUUID();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("accessToken", newToken);
        response.put("refreshToken", newRefresh);
        response.put("tokenType", "Bearer");
        response.put("expiresIn", 86400);

        return ResponseEntity.ok(response);
    }
}
