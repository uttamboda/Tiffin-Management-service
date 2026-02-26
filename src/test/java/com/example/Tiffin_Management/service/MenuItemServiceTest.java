package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.request.MenuItemRequestDTO;
import com.example.Tiffin_Management.dto.response.MenuItemResponseDTO;
import com.example.Tiffin_Management.entity.MenuItem;
import com.example.Tiffin_Management.exception.ResourceNotFoundException;
import com.example.Tiffin_Management.repository.MenuItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    @Test
    void createMenuItem_Success() {
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO();
        requestDTO.setDishName("Pizza");
        requestDTO.setPriceDefault(new BigDecimal("299.00"));

        MenuItem savedItem = new MenuItem();
        savedItem.setId(1L);
        savedItem.setDishName("Pizza");
        savedItem.setPriceDefault(new BigDecimal("299.00"));

        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(savedItem);

        MenuItemResponseDTO responseDTO = menuItemService.createMenuItem(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("Pizza", responseDTO.getDishName());
        assertEquals(new BigDecimal("299.00"), responseDTO.getPriceDefault());
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void getAllMenuItems_ReturnsList() {
        MenuItem item1 = new MenuItem();
        item1.setId(1L);
        item1.setDishName("Burger");
        item1.setPriceDefault(new BigDecimal("150.00"));

        when(menuItemRepository.findAll()).thenReturn(List.of(item1));

        List<MenuItemResponseDTO> result = menuItemService.getAllMenuItems();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Burger", result.get(0).getDishName());
        verify(menuItemRepository, times(1)).findAll();
    }

    @Test
    void updateMenuItem_Success() {
        Long id = 1L;
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO();
        requestDTO.setDishName("Updated Pizza");
        requestDTO.setPriceDefault(new BigDecimal("350.00"));

        MenuItem existingItem = new MenuItem();
        existingItem.setId(id);
        existingItem.setDishName("Pizza");
        existingItem.setPriceDefault(new BigDecimal("299.00"));

        MenuItem updatedItem = new MenuItem();
        updatedItem.setId(id);
        updatedItem.setDishName("Updated Pizza");
        updatedItem.setPriceDefault(new BigDecimal("350.00"));

        when(menuItemRepository.findById(id)).thenReturn(Optional.of(existingItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedItem);

        MenuItemResponseDTO responseDTO = menuItemService.updateMenuItem(id, requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Updated Pizza", responseDTO.getDishName());
        assertEquals(new BigDecimal("350.00"), responseDTO.getPriceDefault());
        verify(menuItemRepository, times(1)).findById(id);
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void updateMenuItem_NotFound_ThrowsException() {
        Long id = 1L;
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO();

        when(menuItemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.updateMenuItem(id, requestDTO));
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void deleteMenuItem_Success() {
        Long id = 1L;
        when(menuItemRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> menuItemService.deleteMenuItem(id));
        verify(menuItemRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteMenuItem_NotFound_ThrowsException() {
        Long id = 1L;
        when(menuItemRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.deleteMenuItem(id));
        verify(menuItemRepository, never()).deleteById(anyLong());
    }
}
