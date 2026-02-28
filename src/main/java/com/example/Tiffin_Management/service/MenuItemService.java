package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.request.MenuItemRequestDTO;
import com.example.Tiffin_Management.dto.response.MenuItemResponseDTO;
import com.example.Tiffin_Management.entity.MenuItem;
import com.example.Tiffin_Management.exception.ResourceNotFoundException;
import com.example.Tiffin_Management.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final ShopService shopService;

    public MenuItemResponseDTO createMenuItem(MenuItemRequestDTO requestDTO) {
        MenuItem menuItem = new MenuItem();
        menuItem.setDishName(requestDTO.getDishName());
        menuItem.setPriceDefault(requestDTO.getPriceDefault());
        menuItem.setShop(shopService.getCurrentShop());

        MenuItem saved = menuItemRepository.save(menuItem);
        return mapToResponseDTO(saved);
    }

    public List<MenuItemResponseDTO> getAllMenuItems() {
        Long shopId = shopService.getCurrentShop().getId();
        return menuItemRepository.findAllByShop_Id(shopId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public MenuItemResponseDTO updateMenuItem(Long id, MenuItemRequestDTO requestDTO) {
        Long shopId = shopService.getCurrentShop().getId();
        MenuItem existing = menuItemRepository.findByIdAndShop_Id(id, shopId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("MenuItem not found with id: " + id + " in current shop"));

        existing.setDishName(requestDTO.getDishName());
        existing.setPriceDefault(requestDTO.getPriceDefault());

        MenuItem saved = menuItemRepository.save(existing);
        return mapToResponseDTO(saved);
    }

    public void deleteMenuItem(Long id) {
        Long shopId = shopService.getCurrentShop().getId();
        MenuItem existing = menuItemRepository.findByIdAndShop_Id(id, shopId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("MenuItem not found with id: " + id + " in current shop"));
        menuItemRepository.delete(existing);
    }

    private MenuItemResponseDTO mapToResponseDTO(MenuItem menuItem) {
        MenuItemResponseDTO dto = new MenuItemResponseDTO();
        dto.setId(menuItem.getId());
        dto.setDishName(menuItem.getDishName());
        dto.setPriceDefault(menuItem.getPriceDefault());
        return dto;
    }
}
