package com.example.maumdiary.controller;

import com.example.maumdiary.dto.ChatGptResponseDto;
import com.example.maumdiary.entity.DiaryRepository;
import com.example.maumdiary.service.ChatGptService;
import com.example.maumdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatgpt")
public class ChatgptController {
    private final ChatGptService chatgptService;
    private final UserService userService;
    private final DiaryRepository diaryRepository;

    @Autowired
    public ChatgptController(ChatGptService chatgptService, UserService userService, DiaryRepository diaryRepository) {
        this.chatgptService = chatgptService;
        this.userService = userService;
        this.diaryRepository = diaryRepository;
    }

    // chat-gpt와 간단한 채팅 서비스 소스
    @PostMapping("/diary")
    public ResponseEntity<ChatGptResponseDto> writingDiary(@RequestParam Long userId) {
        String chatContent = userService.concatChat(userId);

        ChatGptResponseDto chatGptResponseDto;
        try {
            String question = "아래 내용은 내가 오늘 하루동안 '나와의 채팅방'에 쓴 내용들이야. 아래 내용을 이용해서 100자 이내의 일기를 써줘.\n\n'" + chatContent + "'";
            chatGptResponseDto = chatgptService.askQuestion(question);
            chatgptService.saveDiary(userId, chatGptResponseDto.getChoices().get(0).getMessage().getContent());
        } catch (Exception e) {
            System.out.println("e.getMessage() : " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok().body(chatGptResponseDto);
    }
}
