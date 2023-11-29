package com.example.maumdiary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DiaryDTO {
    private Long userId;
    private String content;
    private LocalDate createdAt;

    public DiaryDTO(Long userId, String content, LocalDate createdAt) {
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
