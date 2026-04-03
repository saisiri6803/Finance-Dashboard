package com.finance.dto.request;

import com.finance.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Size(min = 2, max = 60)
    private String name;
    private Role role;
    private Boolean active;
}