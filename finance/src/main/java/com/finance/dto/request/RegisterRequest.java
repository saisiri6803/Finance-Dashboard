package com.finance.dto.request;

import com.finance.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(min = 2, max = 60)
    private String name;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6)
    private String password;
    @NotNull
    private Role role;
}