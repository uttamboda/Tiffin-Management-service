package com.example.Tiffin_Management.controller;

import com.example.Tiffin_Management.dto.request.TenantLoginRequestDTO;
import com.example.Tiffin_Management.dto.request.TenantRequestDTO;
import com.example.Tiffin_Management.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody TenantRequestDTO request) {
        try {
            return ResponseEntity.ok(tenantService.register(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody TenantLoginRequestDTO request) {
        try {
            return ResponseEntity.ok(tenantService.login(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            tenantService.logout(token);
            return ResponseEntity.ok("Successfully logged out");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to logout");
        }
    }
}
