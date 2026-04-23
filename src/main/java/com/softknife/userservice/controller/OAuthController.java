package com.softknife.userservice.controller;

import com.softknife.userservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final JwtService jwtService;

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> tokenForm(
            @RequestParam("grant_type") String grantType,
            @RequestParam(value = "client_id", required = false) String clientId,
            @RequestParam(value = "client_secret", required = false) String clientSecret,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "scope", required = false) String scope) {

        if ("client_credentials".equals(grantType)) {
            String token = jwtService.generateToken(clientId != null ? clientId : "client", "ADMIN");
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("access_token", token);
            response.put("token_type", "Bearer");
            response.put("expires_in", 86400);
            response.put("scope", scope != null ? scope : "read:users write:users");
            return ResponseEntity.ok(response);
        }

        if ("password".equals(grantType)) {
            String token = jwtService.generateToken(username != null ? username : "user", "USER");
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("access_token", token);
            response.put("id_token", jwtService.generateToken(username != null ? username : "user", "USER"));
            response.put("refresh_token", "oauth2-refresh-" + java.util.UUID.randomUUID());
            response.put("token_type", "Bearer");
            response.put("expires_in", 86400);
            response.put("scope", "openid profile email");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(Map.of(
                "error", "unsupported_grant_type",
                "error_description", "Grant type '" + grantType + "' is not supported"
        ));
    }

    @PostMapping(value = "/token", consumes = "application/json")
    public ResponseEntity<?> tokenJson(@RequestBody Map<String, String> request) {
        String clientId = request.get("client_id");
        String token = jwtService.generateToken(clientId != null ? clientId : "client", "ADMIN");
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("access_token", token);
        response.put("token_type", "Bearer");
        response.put("expires_in", 86400);
        return ResponseEntity.ok(response);
    }
}
