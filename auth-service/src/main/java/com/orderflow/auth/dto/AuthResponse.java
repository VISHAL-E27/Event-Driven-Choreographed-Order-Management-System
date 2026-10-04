package com.orderflow.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing issued JWT token and user info")
public class AuthResponse {

    @Schema(description = "JWT Bearer access token", example = "eyJhbGciOiJSUzI1NiJ9...")
    private String token;

    @Schema(description = "TokenType prefix", example = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "Authenticated username", example = "vish_1")
    private String username;

    @Schema(description = "User email address", example = "vish.xyz@example.com")
    private String email;

    @Schema(description = "User assigned role", example = "ROLE_USER")
    private String role;

    @Schema(description = "Token lifetime in milliseconds", example = "3600000")
    @Builder.Default
    private long expiresIn = 3600000;
}
