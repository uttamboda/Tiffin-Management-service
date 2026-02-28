package com.example.Tiffin_Management.controller;

import com.example.Tiffin_Management.dto.request.TenantLoginRequestDTO;
import com.example.Tiffin_Management.dto.request.TenantRequestDTO;
import com.example.Tiffin_Management.service.TenantService;
import com.example.Tiffin_Management.dto.response.TokenResponseDTO;
import com.example.Tiffin_Management.service.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;
    private final UserSessionService userSessionService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody TenantRequestDTO request) {
        try {
            return ResponseEntity.ok(tenantService.register(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody TenantLoginRequestDTO request, HttpServletRequest httpRequest) {
        try {
            TokenResponseDTO response = tenantService.login(request);
            // Create user session immediately after successful login using the new token
            userSessionService.createSession(request.getUsername(), httpRequest.getRemoteAddr(), response.getToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            tenantService.logout(token);

            // Clean up Bearer prefix before using token to close session
            String localToken = token;
            if (localToken != null && localToken.startsWith("Bearer ")) {
                localToken = localToken.substring(7);
            }
            userSessionService.endSessionBySessionId(localToken);

            return ResponseEntity.ok("Successfully logged out");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to logout");
        }
    }
}
