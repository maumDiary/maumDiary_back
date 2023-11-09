package com.example.maumdiary.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatDTO {
    private Long userId;
    private String content;
    private LocalDateTime datetime;

    public ChatDTO(Long userId, String content, LocalDateTime datetime) {
        this.userId = userId;
        this.content = content;
        this.datetime = datetime;
    }
}
