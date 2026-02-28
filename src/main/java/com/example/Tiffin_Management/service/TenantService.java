package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.request.TenantLoginRequestDTO;
import com.example.Tiffin_Management.dto.request.TenantRequestDTO;
import com.example.Tiffin_Management.dto.response.TenantResponseDTO;
import com.example.Tiffin_Management.dto.response.TokenResponseDTO;

public interface TenantService {
    TenantResponseDTO register(TenantRequestDTO dto);

    TokenResponseDTO login(TenantLoginRequestDTO dto);

    void logout(String token);
}
