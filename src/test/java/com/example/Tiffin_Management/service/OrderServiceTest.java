package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.request.OrderItemRequestDTO;
import com.example.Tiffin_Management.dto.request.OrderRequestDTO;
import com.example.Tiffin_Management.dto.response.OrderResponseDTO;
import com.example.Tiffin_Management.entity.MenuItem;
import com.example.Tiffin_Management.entity.Order;
import com.example.Tiffin_Management.entity.User;
import com.example.Tiffin_Management.exception.ResourceNotFoundException;
import com.example.Tiffin_Management.repository.MenuItemRepository;
import com.example.Tiffin_Management.repository.OrderRepository;
import com.example.Tiffin_Management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_Success_TotalCalculationCorrect() {
        // Arrange
        Long userId = 1L;
        Long menuId1 = 101L;
        Long menuId2 = 102L;

        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setUserId(userId);

        OrderItemRequestDTO item1 = new OrderItemRequestDTO();
        item1.setMenuId(menuId1);
        item1.setQuantity(2);
        item1.setSellingPrice(new BigDecimal("100.00")); // Override price

        OrderItemRequestDTO item2 = new OrderItemRequestDTO();
        item2.setMenuId(menuId2);
        item2.setQuantity(1);
        item2.setSellingPrice(null); // Fallback to default price

        requestDTO.setItems(List.of(item1, item2));

        User user = new User();
        user.setId(userId);
        user.setName("Test User");

        MenuItem menu1 = new MenuItem();
        menu1.setId(menuId1);
        menu1.setDishName("Dish 1");
        menu1.setPriceDefault(new BigDecimal("120.00"));

        MenuItem menu2 = new MenuItem();
        menu2.setId(menuId2);
        menu2.setDishName("Dish 2");
        menu2.setPriceDefault(new BigDecimal("150.00"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(menuItemRepository.findAllById(anyList())).thenReturn(List.of(menu1, menu2));

        Order savedOrder = new Order();
        savedOrder.setId(10L);
        savedOrder.setUser(user);
        savedOrder.setOrderDate(LocalDate.now());
        savedOrder.setStatus("CREATED");
        savedOrder.setItems(new ArrayList<>());
        savedOrder.setTotalAmount(new BigDecimal("350.00")); // (2 * 100) + (1 * 150)

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        OrderResponseDTO responseDTO = orderService.createOrder(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(10L, responseDTO.getId());
        assertEquals(new BigDecimal("350.00"), responseDTO.getTotalAmount());
        assertEquals("CREATED", responseDTO.getStatus());
        verify(userRepository, times(1)).findById(userId);
        verify(menuItemRepository, times(1)).findAllById(anyList());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_InvalidUser_ThrowsException() {
        // Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setUserId(99L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.createOrder(requestDTO));
        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findById(99L);
        verify(menuItemRepository, never()).findAllById(anyList());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_InvalidMenuItem_ThrowsException() {
        // Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setUserId(1L);

        OrderItemRequestDTO item1 = new OrderItemRequestDTO();
        item1.setMenuId(999L);
        item1.setQuantity(1);
        requestDTO.setItems(List.of(item1));

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // Return empty list as menu is not found
        when(menuItemRepository.findAllById(anyList())).thenReturn(List.of());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.createOrder(requestDTO));
        assertTrue(exception.getMessage().contains("MenuItem not found"));
    }

    @Test
    void getOrderById_Success() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        User user = new User();
        user.setId(10L);
        user.setName("Test User");
        order.setUser(user);
        order.setTotalAmount(new BigDecimal("200.00"));
        order.setStatus("CREATED");
        order.setItems(new ArrayList<>());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        OrderResponseDTO response = orderService.getOrderById(orderId);

        // Assert
        assertNotNull(response);
        assertEquals(orderId, response.getId());
        assertEquals("Test User", response.getUserName());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void getOrderById_NotFound_ThrowsException() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void updateOrderStatus_Success() {
        Long orderId = 1L;
        String newStatus = "COMPLETED";

        Order order = new Order();
        order.setId(orderId);
        order.setStatus("CREATED");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(orderId, newStatus);

        assertEquals("COMPLETED", order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }
}
