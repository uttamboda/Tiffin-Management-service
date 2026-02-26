package com.example.Tiffin_Management.controller;

import com.example.Tiffin_Management.dto.request.OrderRequestDTO;
import com.example.Tiffin_Management.dto.request.OrderStatusUpdateDTO;
import com.example.Tiffin_Management.dto.response.OrderResponseDTO;
import com.example.Tiffin_Management.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void createOrder_Success() throws Exception {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setUserId(1L);
        com.example.Tiffin_Management.dto.request.OrderItemRequestDTO item = new com.example.Tiffin_Management.dto.request.OrderItemRequestDTO();
        item.setMenuId(1L);
        item.setQuantity(1);
        requestDTO.setItems(java.util.Collections.singletonList(item));

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setUserId(1L);
        responseDTO.setTotalAmount(new BigDecimal("500.00"));
        responseDTO.setStatus("CREATED");

        when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void createOrder_BadRequest() throws Exception {
        OrderRequestDTO invalidRequest = new OrderRequestDTO();

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOrders_Success() throws Exception {
        OrderResponseDTO order1 = new OrderResponseDTO();
        order1.setId(1L);
        order1.setStatus("CREATED");

        Page<OrderResponseDTO> page = new PageImpl<>(List.of(order1),
                org.springframework.data.domain.PageRequest.of(0, 10), 1);

        when(orderService.getAllOrders(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/orders")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    void getOrderById_Success() throws Exception {
        OrderResponseDTO order = new OrderResponseDTO();
        order.setId(1L);
        order.setTotalAmount(new BigDecimal("100.00"));

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalAmount").value(100.00));
    }

    @Test
    void updateOrderStatus_Success() throws Exception {
        OrderStatusUpdateDTO updateDTO = new OrderStatusUpdateDTO();
        updateDTO.setStatus("COMPLETED");

        doNothing().when(orderService).updateOrderStatus(eq(1L), anyString());

        mockMvc.perform(put("/orders/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order status updated successfully"));
    }
}
