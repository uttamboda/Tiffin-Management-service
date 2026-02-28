package com.example.Tiffin_Management.controller;

import com.example.Tiffin_Management.dto.request.ShopRequestDTO;
import com.example.Tiffin_Management.security.JwtAuthenticationToken;
import com.example.Tiffin_Management.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/register")
    public ResponseEntity<?> registerShop(@Valid @RequestBody ShopRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || !(auth instanceof JwtAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Tenant must be logged in.");
        }

        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
        Long tenantId = jwtAuth.getTenantId();

        try {
            return ResponseEntity.ok(shopService.registerShop(request, tenantId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering shop: " + e.getMessage());
        }
    }
}
