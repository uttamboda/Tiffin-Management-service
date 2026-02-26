package com.example.Tiffin_Management.controller;

import com.example.Tiffin_Management.dto.request.MenuItemRequestDTO;
import com.example.Tiffin_Management.dto.response.MenuItemResponseDTO;
import com.example.Tiffin_Management.service.MenuItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MenuItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MenuItemService menuItemService;

    @InjectMocks
    private MenuItemController menuItemController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuItemController).build();
    }

    @Test
    void createMenuItem_Success() throws Exception {
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO();
        requestDTO.setDishName("Pasta");
        requestDTO.setPriceDefault(new BigDecimal("200.00"));

        MenuItemResponseDTO responseDTO = new MenuItemResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setDishName("Pasta");
        responseDTO.setPriceDefault(new BigDecimal("200.00"));

        when(menuItemService.createMenuItem(any(MenuItemRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/menu_item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.dishName").value("Pasta"))
                .andExpect(jsonPath("$.priceDefault").value(200.00));
    }

    @Test
    void getAllMenuItems_Success() throws Exception {
        MenuItemResponseDTO responseDTO = new MenuItemResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setDishName("Pasta");

        when(menuItemService.getAllMenuItems()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/menu_item")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].dishName").value("Pasta"));
    }

    @Test
    void updateMenuItem_Success() throws Exception {
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO();
        requestDTO.setDishName("Updated Pasta");
        requestDTO.setPriceDefault(new BigDecimal("250.00"));

        MenuItemResponseDTO responseDTO = new MenuItemResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setDishName("Updated Pasta");
        responseDTO.setPriceDefault(new BigDecimal("250.00"));

        when(menuItemService.updateMenuItem(eq(1L), any(MenuItemRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/menu_item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dishName").value("Updated Pasta"))
                .andExpect(jsonPath("$.priceDefault").value(250.00));
    }

    @Test
    void deleteMenuItem_Success() throws Exception {
        doNothing().when(menuItemService).deleteMenuItem(1L);

        mockMvc.perform(delete("/menu_item/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
