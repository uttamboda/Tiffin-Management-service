package com.example.Tiffin_Management.controller;

import com.example.Tiffin_Management.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import com.example.Tiffin_Management.entity.UserSession;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAnalyticsController {

    private final UserSessionService userSessionService;

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        return ResponseEntity.ok(userSessionService.getAnalytics());
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<UserSession>> getAllSessions() {
        return ResponseEntity.ok(userSessionService.getAllSessions());
    }
}
