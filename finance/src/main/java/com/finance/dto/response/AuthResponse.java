package com.finance.dto.response;

import com.finance.model.Role;
import lombok.*;

@Data @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String name;
    private String email;
    private Role role;
}