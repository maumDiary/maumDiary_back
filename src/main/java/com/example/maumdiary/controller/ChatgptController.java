package com.example.maumdiary.controller;

import com.example.maumdiary.dto.ChatGptDiaryDTO;
import com.example.maumdiary.dto.ChatGptResponseDto;
import com.example.maumdiary.dto.ResponseDTO;
import com.example.maumdiary.service.ChatGptService;
import com.example.maumdiary.service.JwtService;
import com.example.maumdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/chatgpt")
public class ChatgptController {
    private final ChatGptService chatgptService;
    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public ChatgptController(ChatGptService chatgptService, UserService userService, JwtService jwtService) {
        this.chatgptService = chatgptService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // chat-gpt와 간단한 채팅 서비스 소스
    @PostMapping("/diary")
    public ResponseDTO<ChatGptDiaryDTO> writingDiary(@RequestHeader("Authorization") String accessToken,
                                                     @RequestParam Long userId) {
        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "토큰이 만료되었습니다.", null);
        }

        String chatContent = userService.concatChat(userId);

        ChatGptResponseDto chatGptResponseDto;

        // 오전 8시 이전에 <도비> 클릭 시 자동 일기쓰기 불가
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate todayDate = LocalDate.now();
        LocalTime today8am = LocalTime.of(8,0);
        LocalDateTime morning8am = LocalDateTime.of(todayDate, today8am);
        if (localDateTime.isBefore(morning8am)) {
            return new ResponseDTO<>(403, false, "08시 이후부터 자동 일기 쓰기가 가능합니다.", null);
        }

        // 채팅 내용이 적거나 없으면 자동 일기쓰기 불가
        if (chatContent == null | chatContent.length() < 100) {
            return new ResponseDTO<>(404, false, "채팅 내용이 없거나 부족합니다.", null);
        }

        try {
            String question = "아래 내용은 내가 오늘 하루동안 '나와의 채팅방'에 쓴 내용들이야. 아래 내용을 이용해서 100자 이내의 일기를 써줘.\n\n'" + chatContent + "'";
            chatGptResponseDto = chatgptService.askQuestion(question);

            // 일기 내용 db에 저장
            chatgptService.saveDiary(userId, chatGptResponseDto.getChoices().get(0).getMessage().getContent());
        } catch (Exception e) {
            System.out.println("e.getMessage() : " + e.getMessage());
            return new ResponseDTO<>(400, false, "자동 일기를 쓰는 데 실패하였습니다.", null);
        }

        ChatGptDiaryDTO chatGptDiaryDTO = new ChatGptDiaryDTO(userId, chatGptResponseDto.getCreated(), chatGptResponseDto.getChoices().get(0).getMessage().getContent());
        return new ResponseDTO<>(201, true, "일기를 저장하였습니다.", chatGptDiaryDTO);
    }
}
