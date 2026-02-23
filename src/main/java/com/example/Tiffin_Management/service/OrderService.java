package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.*;
import com.example.Tiffin_Management.entity.*;
import com.example.Tiffin_Management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDTO : orderRequestDTO.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemDTO.getMenuId())
                    .orElseThrow(() -> new RuntimeException("MenuItem not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenu(menuItem);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setSellingPrice(menuItem.getPriceDefault());

            BigDecimal subtotal = menuItem.getPriceDefault().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            orderItem.setItemSubtotal(subtotal);

            totalAmount = totalAmount.add(subtotal);
            order.getItems().add(orderItem);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return mapToResponseDTO(savedOrder);
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToResponseDTO(order);
    }

    @Transactional
    public void updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setUserName(order.getUser().getName());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());

        List<OrderItemResponseDTO> itemDTOs = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
                itemDTO.setId(item.getId());
                itemDTO.setMenuId(item.getMenu().getId());
                itemDTO.setDishName(item.getMenu().getDishName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setSellingPrice(item.getSellingPrice());
                itemDTO.setItemSubtotal(item.getItemSubtotal());
                itemDTOs.add(itemDTO);
            }
        }
        dto.setItems(itemDTOs);
        return dto;
    }
}
