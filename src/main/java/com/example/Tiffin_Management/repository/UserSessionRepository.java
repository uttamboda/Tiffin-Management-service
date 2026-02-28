package com.example.Tiffin_Management.repository;

import com.example.Tiffin_Management.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findBySessionId(String sessionId);

    @Query("SELECT COUNT(s) FROM UserSession s WHERE s.loginTime >= :startOfDay")
    long countLoginsSince(LocalDateTime startOfDay);

    @Query("SELECT COUNT(s) FROM UserSession s WHERE s.logoutTime IS NULL")
    long countActiveUsers();

    @Query("SELECT COUNT(DISTINCT s.username) FROM UserSession s WHERE s.role = 'GUEST'")
    long countGuestUsers();

    @Query("SELECT AVG(s.sessionDurationMinutes) FROM UserSession s WHERE s.sessionDurationMinutes IS NOT NULL")
    Double getAverageSessionTimeMinutes();
}
