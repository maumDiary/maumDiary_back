package com.example.maumdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SaveChatReqDTO {
    private Long userId;
    private String content;
}
