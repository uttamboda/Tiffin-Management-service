package com.example.Tiffin_Management.controller;

import com.example.Tiffin_Management.dto.request.OrderRequestDTO;
import com.example.Tiffin_Management.dto.response.OrderResponseDTO;
import com.example.Tiffin_Management.dto.request.OrderStatusUpdateDTO;
import com.example.Tiffin_Management.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        return new ResponseEntity<>(orderService.createOrder(orderRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getAllOrders(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateDTO statusUpdateDTO) {
        orderService.updateOrderStatus(id, statusUpdateDTO.getStatus());
        return ResponseEntity.ok(java.util.Collections.singletonMap("message", "Order status updated successfully"));
    }
}
