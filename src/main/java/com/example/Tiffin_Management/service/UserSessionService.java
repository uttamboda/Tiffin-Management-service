package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.entity.UserSession;
import com.example.Tiffin_Management.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    public void createSession(String username, String ipAddress, String sessionId) {
        UserSession session = new UserSession();
        session.setUsername(username);
        session.setRole("guest".equalsIgnoreCase(username) ? "GUEST" : "USER");
        session.setLoginTime(LocalDateTime.now());
        session.setIpAddress(ipAddress);
        session.setSessionId(sessionId);
        userSessionRepository.save(session);
    }

    public void endSessionBySessionId(String sessionId) {
        Optional<UserSession> sessionOpt = userSessionRepository.findBySessionId(sessionId);
        if (sessionOpt.isPresent()) {
            UserSession session = sessionOpt.get();
            if (session.getLogoutTime() == null) {
                session.setLogoutTime(LocalDateTime.now());
                long duration = Duration.between(session.getLoginTime(), session.getLogoutTime()).toMinutes();
                session.setSessionDurationMinutes(duration);
                userSessionRepository.save(session);
            }
        }
    }

    public Map<String, Object> getAnalytics() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        long totalLoginsToday = userSessionRepository.countLoginsSince(startOfDay);
        long activeUsers = userSessionRepository.countActiveUsers();
        long guestUserCount = userSessionRepository.countGuestUsers();
        Double avgSessionTime = userSessionRepository.getAverageSessionTimeMinutes();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLoginsToday", totalLoginsToday);
        stats.put("activeUsers", activeUsers);
        stats.put("guestUsersCount", guestUserCount);
        stats.put("averageSessionTime", avgSessionTime != null ? Math.round(avgSessionTime * 10.0) / 10.0 : 0.0);
        stats.put("currentlyOnlineUsers", activeUsers);

        return stats;
    }

    public List<UserSession> getAllSessions() {
        return userSessionRepository.findAll();
    }
}
