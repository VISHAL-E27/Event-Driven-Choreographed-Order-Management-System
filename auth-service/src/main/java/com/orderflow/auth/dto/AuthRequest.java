package com.orderflow.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload for user login authentication")
public class AuthRequest {

    @NotBlank(message = "Username is required")
    @Schema(description = "User's registered username", example = "john_doe")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "SecretPass123!")
    private String password;
}
