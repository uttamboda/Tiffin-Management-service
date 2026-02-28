package com.example.Tiffin_Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShopRequestDTO {
    @NotBlank(message = "Shop name is required")
    private String shopName;
}
