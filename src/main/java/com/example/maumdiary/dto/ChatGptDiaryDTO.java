package com.example.maumdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ChatGptDiaryDTO {
    private Long userId;
    private LocalDate created;
    private String content;
}
