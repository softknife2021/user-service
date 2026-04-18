package com.softknife.userservice.controller;

import com.softknife.userservice.model.LoginRequest;
import com.softknife.userservice.model.User;
import com.softknife.userservice.service.JwtService;
import com.softknife.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                    return ResponseEntity.ok(Map.of(
                            "accessToken", token,
                            "tokenType", "Bearer",
                            "username", u.getUsername(),
                            "role", u.getRole()
                    ));
                })
                .orElse(ResponseEntity.status(401).body(Map.of(
                        "error", "Invalid credentials"
                )));
    }
}
