package com.example.maumdiary.controller;

import com.example.maumdiary.service.ChatService;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chatgpt")
public class ChatgptController {
    private final ChatService chatService;
    private final ChatgptService chatgptService;

    // chat-gpt와 간단한 채팅 서비스 소스
    @PostMapping("/summary")
    public String summary(@RequestBody String question) throws Exception{
        try {
            return chatService.getChatResponse(question);
        } catch (Exception e) {
            System.out.println("e.getMessage : " + e.getMessage());
            return null;
        }

    }
}
