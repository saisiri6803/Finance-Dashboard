package com.finance.dto.response;

import com.finance.model.*;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;

    public static UserResponse from(User u) {
        return UserResponse.builder()
                .id(u.getId()).name(u.getName()).email(u.getEmail())
                .role(u.getRole()).active(u.isActive()).createdAt(u.getCreatedAt())
                .build();
    }
}