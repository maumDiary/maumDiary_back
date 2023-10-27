package com.example.maumdiary.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ChatGptMessage {
    private String role;
    private String content;

    @Builder
    public ChatGptMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
