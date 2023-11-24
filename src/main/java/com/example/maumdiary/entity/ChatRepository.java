package com.example.maumdiary.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUserIdAndDatetimeBetween(Long userId, LocalDateTime startDatetime, LocalDateTime endDatetime);
    List<Chat> findChatsByUserId(Long userId);
}
