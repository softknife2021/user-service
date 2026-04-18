package com.softknife.userservice.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
    private boolean active;
    private LocalDateTime createdAt;
}
