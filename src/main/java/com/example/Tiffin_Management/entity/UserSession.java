package com.example.Tiffin_Management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String role; // GUEST or USER

    @Column(nullable = false, columnDefinition = "VARCHAR")
    @Convert(converter = LocalDateTimeStringConverter.class)
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "dd MMM yyyy, hh:mm a", timezone = "Asia/Kolkata")
    private LocalDateTime loginTime;

    @Column(columnDefinition = "VARCHAR")
    @Convert(converter = LocalDateTimeStringConverter.class)
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "dd MMM yyyy, hh:mm a", timezone = "Asia/Kolkata")
    private LocalDateTime logoutTime;

    @Column
    private Long sessionDurationMinutes;

    @Column
    private String ipAddress;

    @Column(length = 1000, unique = true, nullable = false)
    private String sessionId; // Based on JWT token or unique ID
}
