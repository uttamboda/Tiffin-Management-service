package com.example.Tiffin_Management.controller;

import com.example.Tiffin_Management.dto.OrderRequestDTO;
import com.example.Tiffin_Management.dto.OrderResponseDTO;
import com.example.Tiffin_Management.dto.OrderStatusUpdateDTO;
import com.example.Tiffin_Management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return new ResponseEntity<>(orderService.createOrder(orderRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id,
            @RequestBody OrderStatusUpdateDTO statusUpdateDTO) {
        try {
            orderService.updateOrderStatus(id, statusUpdateDTO.getStatus());
            return ResponseEntity
                    .ok(java.util.Collections.singletonMap("message", "Order status updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Collections.singletonMap("message", e.getMessage()));
        }
    }
}
