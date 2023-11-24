package com.example.maumdiary.controller;

import com.example.maumdiary.dto.ChatDTO;
import com.example.maumdiary.dto.ColorReqDTO;
import com.example.maumdiary.dto.ResponseDTO;
import com.example.maumdiary.dto.SaveChatReqDTO;
import com.example.maumdiary.entity.Chat;
import com.example.maumdiary.entity.Color;
import com.example.maumdiary.entity.User;
import com.example.maumdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/{user_id}")
    public ResponseDTO<User> getUserById(@PathVariable("user_id") Long userId) {
        User user = userService.getUserByUserId(userId);

        return new ResponseDTO<>(200, true, "사용자 정보를 불러왔습니다.", user);
    }

    @PostMapping("/{user_id}/color")
    public ResponseDTO<Color> saveColor(@PathVariable("user_id") Long userId,
                                        @RequestBody ColorReqDTO requestbody) {

        try {
            userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMeaage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        String colorName = requestbody.getColor_name();
        LocalDate date = requestbody.getDate();
        Color color = userService.saveColor(userId, colorName, date);

        return new ResponseDTO<>(201, true, "색깔이 저장되었습니다.", color);
    }

    @GetMapping("/{user_id}/chat")
    public ResponseDTO<List<Chat>> getChat(@PathVariable("user_id") Long userId) {

        try {
            userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMessage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        List<Chat> chats = userService.getChats(userId);
        return new ResponseDTO<>(200, true, "채팅 내용을 불러왔습니다.", chats);
    }
}
