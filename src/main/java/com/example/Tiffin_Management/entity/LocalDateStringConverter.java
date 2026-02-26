package com.example.Tiffin_Management.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Converter(autoApply = true)
public class LocalDateStringConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.toString();
    }

    @Override
    public LocalDate convertToEntityAttribute(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            // Check if it's stored as milliseconds string from previous implementation
            long millis = Long.parseLong(dateString);
            return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (NumberFormatException e) {
            // Otherwise parse normally
            return LocalDate.parse(dateString);
        }
    }
}
