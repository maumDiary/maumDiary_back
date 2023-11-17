package com.example.maumdiary.controller;

import com.example.maumdiary.dto.ChatDTO;
import com.example.maumdiary.dto.ResponseDTO;
import com.example.maumdiary.dto.SaveChatReqDTO;
import com.example.maumdiary.entity.User;
import com.example.maumdiary.service.ChatGptService;
import com.example.maumdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    // 접속 횟수로 레벨을 정하고 해당 레벨을 db에 저장
    @GetMapping("/{user_id}/level")
    public ResponseDTO<Integer> saveUserLevel(@PathVariable("user_id") Long userId) {
        User user = userService.getUserByUserId(userId);
        int connectNum = user.getConnectNum();
        int level = 0;
        if (connectNum < 10);
        else if (connectNum < 20) level = 1;
        else if (connectNum < 30) level = 2;
        else if (connectNum < 40) level = 3;
        else level = 4;
        userService.updateLevel(userId, level);

        return new ResponseDTO<>(200, true, "사용자의 레벨이 업데이트 되었습니다.", level);
    }
}
