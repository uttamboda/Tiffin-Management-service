package com.example.Tiffin_Management.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequestDTO {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequestDTO> items;
}
