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

    public MenuItemResponseDTO createMenuItem(MenuItemRequestDTO requestDTO) {
        MenuItem menuItem = new MenuItem();
        menuItem.setDishName(requestDTO.getDishName());
        menuItem.setPriceDefault(requestDTO.getPriceDefault());

        MenuItem saved = menuItemRepository.save(menuItem);
        return mapToResponseDTO(saved);
    }

    public List<MenuItemResponseDTO> getAllMenuItems() {
        return menuItemRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public MenuItemResponseDTO updateMenuItem(Long id, MenuItemRequestDTO requestDTO) {
        MenuItem existing = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id: " + id));

        existing.setDishName(requestDTO.getDishName());
        existing.setPriceDefault(requestDTO.getPriceDefault());

        MenuItem saved = menuItemRepository.save(existing);
        return mapToResponseDTO(saved);
    }

    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("MenuItem not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }

    private MenuItemResponseDTO mapToResponseDTO(MenuItem menuItem) {
        MenuItemResponseDTO dto = new MenuItemResponseDTO();
        dto.setId(menuItem.getId());
        dto.setDishName(menuItem.getDishName());
        dto.setPriceDefault(menuItem.getPriceDefault());
        return dto;
    }
}
