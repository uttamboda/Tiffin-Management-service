package com.example.Tiffin_Management.service.impl;

import com.example.Tiffin_Management.dto.request.TenantLoginRequestDTO;
import com.example.Tiffin_Management.dto.request.TenantRequestDTO;
import com.example.Tiffin_Management.dto.response.TenantResponseDTO;
import com.example.Tiffin_Management.dto.response.TokenResponseDTO;
import com.example.Tiffin_Management.entity.Tenant;
import com.example.Tiffin_Management.repository.TenantRepository;
import com.example.Tiffin_Management.service.TenantService;
import com.example.Tiffin_Management.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public TenantResponseDTO register(TenantRequestDTO dto) {
        if (tenantRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        Tenant tenant = new Tenant();
        tenant.setName(dto.getName());
        tenant.setUsername(dto.getUsername());
        tenant.setPassword(passwordEncoder.encode(dto.getPassword()));

        Tenant savedTenant = tenantRepository.save(tenant);

        TenantResponseDTO response = new TenantResponseDTO();
        response.setId(savedTenant.getId());
        response.setName(savedTenant.getName());
        response.setUsername(savedTenant.getUsername());

        return response;
    }

    @Override
    public TokenResponseDTO login(TenantLoginRequestDTO dto) {
        Tenant tenant = tenantRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.getPassword(), tenant.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(tenant.getId(), tenant.getUsername());
        return new TokenResponseDTO(token);
    }
}
