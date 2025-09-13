package com.mechtrack.api;

import com.mechtrack.model.dto.JwtResponse;
import com.mechtrack.model.dto.LoginRequest;
import com.mechtrack.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication controller for workshop owner login
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Workshop owner authentication endpoints")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(
        summary = "Workshop owner login",
        description = "Authenticates the workshop owner and returns a JWT token for accessing protected endpoints"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = JwtResponse.class),
                examples = @ExampleObject(
                    name = "Successful Login",
                    value = """
                    {
                        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "type": "Bearer",
                        "username": "mike_johnson",
                        "expiresAt": "2024-01-01T12:00:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Invalid credentials",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Invalid Credentials",
                    value = """
                    {
                        "timestamp": "2024-01-01T12:00:00",
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "Invalid username or password"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<JwtResponse> authenticateUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Login credentials",
            required = true,
            content = @Content(
                schema = @Schema(implementation = LoginRequest.class),
                examples = @ExampleObject(
                    name = "Login Request",
                    value = """
                    {
                        "username": "mike_johnson",
                        "password": "MikeWorkshop2024!"
                    }
                    """
                )
            )
        )
        @Valid @RequestBody LoginRequest loginRequest) {
        
        JwtResponse jwtResponse = authenticationService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/validate")
    @Operation(
        summary = "Validate JWT token",
        description = "Validates if the provided JWT token is still valid and not expired"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token is valid"),
        @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    })
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        AuthenticationService.TokenValidationResult result = authenticationService.validateToken(authHeader);
        
        Map<String, Object> response = new HashMap<>();
        response.put("valid", result.isValid());
        response.put("username", result.getUsername());
        response.put("expiresAt", result.getExpiresAt());
        
        return ResponseEntity.ok(response);
    }
}
