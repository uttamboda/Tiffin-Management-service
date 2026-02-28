package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.request.ShopRequestDTO;
import com.example.Tiffin_Management.dto.response.ShopResponseDTO;
import com.example.Tiffin_Management.entity.Shop;

public interface ShopService {
    ShopResponseDTO registerShop(ShopRequestDTO dto, Long tenantId);

    Shop getCurrentShop();
}
