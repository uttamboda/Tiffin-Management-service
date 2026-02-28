package com.example.Tiffin_Management.service.impl;

import com.example.Tiffin_Management.dto.request.ShopRequestDTO;
import com.example.Tiffin_Management.dto.response.ShopResponseDTO;
import com.example.Tiffin_Management.entity.Shop;
import com.example.Tiffin_Management.entity.TenantShopMap;
import com.example.Tiffin_Management.repository.ShopRepository;
import com.example.Tiffin_Management.repository.TenantShopMapRepository;
import com.example.Tiffin_Management.service.ShopService;
import com.example.Tiffin_Management.util.SecurityUtils;
import com.example.Tiffin_Management.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final TenantShopMapRepository tenantShopMapRepository;

    @Override
    public ShopResponseDTO registerShop(ShopRequestDTO dto, Long tenantId) {
        // 1. Create and save the new shop
        Shop shop = new Shop();
        shop.setShopName(dto.getShopName());

        Shop savedShop = shopRepository.save(shop);

        // 2. Create the tenant-shop mapping
        TenantShopMap mapping = new TenantShopMap();
        mapping.setTenantId(tenantId);
        mapping.setShopId(savedShop.getId());
        mapping.setRole("OWNER");

        tenantShopMapRepository.save(mapping);

        // 3. Return the response
        ShopResponseDTO response = new ShopResponseDTO();
        response.setId(savedShop.getId());
        response.setShopName(savedShop.getShopName());
        response.setMessage("Shop successfully registered and linked to tenant.");

        return response;
    }

    @Override
    public Shop getCurrentShop() {
        Long tenantId = SecurityUtils.getTenantId();
        TenantShopMap mapping = tenantShopMapRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop mapping not found for tenant: " + tenantId));
        return shopRepository.findById(mapping.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with ID: " + mapping.getShopId()));
    }
}
