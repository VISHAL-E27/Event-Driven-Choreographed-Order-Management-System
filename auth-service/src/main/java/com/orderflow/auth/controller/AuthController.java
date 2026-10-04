package com.orderflow.auth.controller;

import com.orderflow.auth.dto.AuthRequest;
import com.orderflow.auth.dto.AuthResponse;
import com.orderflow.auth.dto.RegisterRequest;
import com.orderflow.auth.service.AuthService;
import com.orderflow.auth.service.JwtService;
import com.orderflow.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Management", description = "Endpoints for user registration, authentication, and JWT issuance")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @Operation(summary = "Register User", description = "Registers a new user account and returns a valid RS256 JWT access token")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Username or email already taken / validation failure")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @Operation(summary = "Login User", description = "Authenticates user credentials and returns a valid RS256 JWT access token")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User authenticated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @Operation(summary = "Get Public Key", description = "Retrieves the Base64 RSA Public Key for verifying JWT signatures")
    @GetMapping("/public-key")
    public ResponseEntity<ApiResponse<String>> getPublicKey() {
        String publicKey = jwtService.getPublicKeyBase64();
        return ResponseEntity.ok(ApiResponse.success("Public key retrieved", publicKey));
    }
}
