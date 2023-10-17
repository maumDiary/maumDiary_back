package com.example.maumdiary.service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatgptService chatgptService;

    //Chat-gpt에게 질문을 던지는 메서드
    public String getChatResponse(String content) {
        return chatgptService.sendMessage(content);
    }
}
