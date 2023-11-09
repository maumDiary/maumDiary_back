package com.example.maumdiary.controller;

import com.example.maumdiary.dto.ChatDTO;
import com.example.maumdiary.dto.ResponseDTO;
import com.example.maumdiary.dto.SaveChatReqDTO;
import com.example.maumdiary.service.ChatGptService;
import com.example.maumdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final ChatGptService chatGptService;

    @Autowired
    public UserController(UserService userService, ChatGptService chatGptService) {
        this.userService = userService;
        this.chatGptService = chatGptService;
    }

    // 사용자가 전송한 채팅 내용을 db에 저장
    @PostMapping("/chat")
    public ResponseDTO<ChatDTO> saveChatContents(@RequestBody SaveChatReqDTO requestbody){
        Long userId = requestbody.getUserId();
        String content = requestbody.getContent();
        ChatDTO chatDTO;
        try {
            chatDTO = userService.saveChatContent(userId, content);
        } catch (Exception e) {
            System.out.println("e.getMassage : " + e.getMessage());
            return new ResponseDTO<>(400, false, "채팅이 저장되지 않았습니다.", null);
        }

        return new ResponseDTO<>(200, true, "채팅이 저장되었습니다.", chatDTO);
    }
}
