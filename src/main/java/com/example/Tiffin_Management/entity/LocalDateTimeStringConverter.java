package com.example.Tiffin_Management.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Converter(autoApply = true)
public class LocalDateTimeStringConverter implements AttributeConverter<LocalDateTime, String> {

    // YYYY-MM-DD HH:MM:SS format ensures that standard SQL ALPHABETICAL sorting
    // exactly mirrors true CHRONOLOGICAL sorting.
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String convertToDatabaseColumn(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(FORMATTER);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            // First try parsing standard format saved by this converter
            return LocalDateTime.parse(dateString, FORMATTER);
        } catch (DateTimeParseException e1) {
            try {
                // Check if it's stored as milliseconds string from previous implementation or
                // existing data
                long millis = Long.parseLong(dateString);
                return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
            } catch (NumberFormatException e2) {
                // If it's another ISO format like "2026-02-28T17:45:00" fallback to default
                return LocalDateTime.parse(dateString);
            }
        }
    }
}
