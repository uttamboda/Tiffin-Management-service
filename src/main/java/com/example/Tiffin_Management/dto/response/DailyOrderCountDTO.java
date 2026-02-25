package com.example.Tiffin_Management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyOrderCountDTO {
    private LocalDate date;
    private Long orderCount;
}
