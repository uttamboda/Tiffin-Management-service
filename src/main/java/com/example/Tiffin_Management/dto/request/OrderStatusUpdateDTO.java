package com.example.Tiffin_Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    @NotBlank(message = "Status cannot be empty")
    private String status;
}
