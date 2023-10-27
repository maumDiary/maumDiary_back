package com.example.maumdiary.controller;

import com.example.maumdiary.dto.ChatGptReqDTO;
import com.example.maumdiary.dto.ChatGptResponseDto;
import com.example.maumdiary.service.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatgpt")
public class ChatgptController {
    private final ChatGptService chatgptService;

    @Autowired
    public ChatgptController(ChatGptService chatgptService) {
        this.chatgptService = chatgptService;
    }

    // chat-gpt와 간단한 채팅 서비스 소스
    @PostMapping("/diary")
    public ResponseEntity<ChatGptResponseDto> writingDiary(
            @RequestBody ChatGptReqDTO requestbody) {
        ChatGptResponseDto chatGptResponseDto = null;
        try {
            String question = "이 내용은 오늘 하루동안 내가 '나와의 채팅방'에 보낸 채팅들이야. 아래 채팅 내용을 100자 이내의 일기로 써줘.\n\n" + requestbody.getQuestion();
            chatGptResponseDto = chatgptService.askQuestion(question);
        } catch (Exception e) {
            System.out.println("e.getMessage() : " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok().body(chatGptResponseDto);
    }
}
