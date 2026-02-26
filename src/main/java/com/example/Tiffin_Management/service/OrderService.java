package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.request.*;
import com.example.Tiffin_Management.dto.response.*;
import com.example.Tiffin_Management.entity.*;
import com.example.Tiffin_Management.exception.ResourceNotFoundException;
import com.example.Tiffin_Management.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with id: " + orderRequestDTO.getUserId()));

        Order order = new Order();
        order.setUser(user);

        if (orderRequestDTO.getOrderDate() != null) {
            order.setOrderDate(orderRequestDTO.getOrderDate());
        } else {
            order.setOrderDate(LocalDate.now());
        }

        order.setStatus("CREATED");

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<Long> menuIds = orderRequestDTO.getItems().stream()
                .map(OrderItemRequestDTO::getMenuId)
                .collect(Collectors.toList());

        Map<Long, MenuItem> menuItemMap = menuItemRepository.findAllById(menuIds).stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));

        for (OrderItemRequestDTO itemDTO : orderRequestDTO.getItems()) {
            MenuItem menuItem = menuItemMap.get(itemDTO.getMenuId());
            if (menuItem == null) {
                throw new ResourceNotFoundException("MenuItem not found with id: " + itemDTO.getMenuId());
            }

            BigDecimal price;
            if (itemDTO.getSellingPrice() != null) {
                price = itemDTO.getSellingPrice();
            } else {
                price = menuItem.getPriceDefault();
            }

            log.info("REQUEST SELLING PRICE = {}, FINAL PRICE USED = {}", itemDTO.getSellingPrice(), price);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenu(menuItem);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setSellingPrice(price);

            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            orderItem.setItemSubtotal(subtotal);

            totalAmount = totalAmount.add(subtotal);
            order.getItems().add(orderItem);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return mapToResponseDTO(savedOrder);
    }

    public Page<OrderResponseDTO> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size))
                .map(this::mapToResponseDTO);
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponseDTO(order);
    }

    @Transactional
    public void updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
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
