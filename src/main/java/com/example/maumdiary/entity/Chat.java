package com.example.maumdiary.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "chat")
public class Chat implements Serializable {
    @Id
    @Column(name = "chat_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;

    @Builder
    public Chat(Long chatId, Long userId, String content, LocalDateTime datetime) {
        this.chatId = chatId;
        this.userId = userId;
        this.content = content;
        this.datetime = datetime;
    }
}
