package com.example.maumdiary.controller;

import com.example.maumdiary.dto.ResponseDTO;
import com.example.maumdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/gpt")
    public ResponseDTO<?> AutoDiaryByChatGPT() {
        return new ResponseDTO<>(200, true, "일기 작성이 완료되었습니다.", null);
    }
}
