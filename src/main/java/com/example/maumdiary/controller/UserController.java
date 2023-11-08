package com.example.maumdiary.controller;

import com.example.maumdiary.dto.ResponseDTO;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자가 전송한 채팅 내용을 db에 저장
    @PostMapping("/chat")
    public ResponseDTO<?> saveChatContents(@RequestBody String content) {

        return new ResponseDTO<>(200, true, "일기 작성이 완료되었습니다.", null);
    }
}
