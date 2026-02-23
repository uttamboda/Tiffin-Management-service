package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.entity.MenuItem;
import com.example.Tiffin_Management.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public MenuItem createMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public MenuItem updateMenuItem(Long id, MenuItem updatedMenuItem) {
        MenuItem existing = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuItem not found"));
        existing.setDishName(updatedMenuItem.getDishName());
        existing.setPriceDefault(updatedMenuItem.getPriceDefault());
        return menuItemRepository.save(existing);
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
