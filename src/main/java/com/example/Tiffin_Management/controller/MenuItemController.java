package com.example.Tiffin_Management.controller;

import com.example.Tiffin_Management.dto.request.MenuItemRequestDTO;
import com.example.Tiffin_Management.dto.response.MenuItemResponseDTO;
import com.example.Tiffin_Management.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu_item")
@RequiredArgsConstructor
public class MenuItemController {
    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(@Valid @RequestBody MenuItemRequestDTO requestDTO) {
        return new ResponseEntity<>(menuItemService.createMenuItem(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponseDTO>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItem(@PathVariable Long id,
            @Valid @RequestBody MenuItemRequestDTO requestDTO) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}
