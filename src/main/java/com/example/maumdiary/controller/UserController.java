package com.example.maumdiary.controller;

import com.example.maumdiary.dto.*;
import com.example.maumdiary.entity.Chat;
import com.example.maumdiary.entity.Color;
import com.example.maumdiary.entity.Diary;
import com.example.maumdiary.entity.User;
import com.example.maumdiary.service.GoogleLoginService;
import com.example.maumdiary.service.JwtService;
import com.example.maumdiary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final GoogleLoginService googleLoginService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseDTO<JwtDTO> registerUser(@RequestHeader("Authorization") String token,
                                            @RequestBody SignupDTO requestbody) throws GeneralSecurityException, IOException {
        String socialId = googleLoginService.getSocialIdByToken(token);
        // 유효하지 않은 토큰의 경우 socialId를 null로 리턴
        if (socialId == null) {
            return new ResponseDTO<>(401, false, "토큰이 유효하지 않습니다.", null);
        }

        // 사용자 db에 저장
        User user = new User("google", socialId, requestbody.getNickname(), 1, 1);
        user = userService.insertUser(user);

        // JWT 전용 토큰을 생성하여 리턴
        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
        JwtDTO jwtDTO = new JwtDTO(user.getId(), accessToken, refreshToken);

        return new ResponseDTO<>(201, true, "회원가입 완료하였습니다.", jwtDTO);
    }

    // 닉네임 변경
    @PatchMapping("/{user_id}")
    public ResponseDTO<UserDTO> changeNickname(@RequestHeader("Authorization") String accessToken,
                                               @PathVariable("user_id") Long userId,
                                               @RequestParam("nickname") String nickname) {
        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }

        User user;
        try {
            user = userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMeaage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        user.setNickname(nickname);
        userService.updateUser(user);

        UserDTO userDTO = new UserDTO(userId, nickname, user.getLevel(), user.getExp());

        return new ResponseDTO<>(200, true, "닉네임이 변경되었습니다.", userDTO);
    }

    // 회원 탈퇴
    @DeleteMapping("/{user_id}")
    public ResponseDTO<UserDTO> deleteUser(@RequestHeader("Authorization") String accessToken,
                                           @PathVariable("user_id") Long userId) {
        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }

        User user;
        try {
            user = userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMeaage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }
        UserDTO userDTO = new UserDTO(userId, user.getNickname(), user.getLevel(), user.getExp());

        // 유저 삭제
        userService.deleteUser(user);

        return new ResponseDTO<>(200, true, "탈퇴가 완료되었습니다.", userDTO);
    }

    // 사용자가 전송한 채팅 내용을 db에 저장
    @PostMapping("/chat")
    public ResponseDTO<ChatDTO> saveChatContents(@RequestHeader("Authorization") String accessToken,
                                                 @RequestBody SaveChatReqDTO requestbody){
        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }

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

    // 사용자 정보 조회
    @GetMapping("/{user_id}")
    public ResponseDTO<User> getUserById(@PathVariable("user_id") Long userId) {
        User user;
        try {
            user = userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMessage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        return new ResponseDTO<>(200, true, "사용자 정보를 불러왔습니다.", user);
    }

    // 색깔 저장
    @PostMapping("/{user_id}/color")
    public ResponseDTO<Color> saveColor(@RequestHeader("Authorization") String accessToken,
                                        @PathVariable("user_id") Long userId,
                                        @RequestBody ColorReqDTO requestbody) {
        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }

        try {
            userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMeaage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        // 색깔 db에 저장
        String colorName = requestbody.getColor_name();
        LocalDate date = requestbody.getDate();
        Color color = userService.saveColor(userId, colorName, date);

        // 사용자 exp 증가, level 업데이트
        userService.updateLevel(userId);

        return new ResponseDTO<>(201, true, "색깔이 저장되었습니다.", color);
    }

    @GetMapping("/{user_id}/chat")
    public ResponseDTO<List<Chat>> getChats(@RequestHeader("Authorization") String accessToken,
                                            @PathVariable("user_id") Long userId) {
        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }

        try {
            userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMeaage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        List<Chat> chats = userService.getChats(userId);
        return new ResponseDTO<>(200, true, "채팅 내용을 불러왔습니다.", chats);
    }

    @GetMapping("/{user_id}/today")
    public ResponseDTO<Color> todayColor(@RequestHeader("Authorization") String accessToken,
                                         @PathVariable("user_id") Long userId) {
        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }

        try {
            userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMeaage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        LocalDate today = LocalDate.now();
        Color color = userService.getColor(userId, today);

        return new ResponseDTO<>(200, true, "색깔을 불러왔습니다.", color);
    }

    @GetMapping("/{user_id}/month")
    public ResponseDTO<List<Color>> getMonthlyColor(@RequestHeader("Authorization") String accessToken,
                                                    @PathVariable("user_id") Long userId,
                                                    @RequestParam("date") String date) throws ParseException {
        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }

        try {
            userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMeaage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date month = simpleDateFormat.parse(date);
        List<Color> colors;

        try {
             colors = userService.getMonthlyColor(userId, month);
        } catch (Exception e) {
            return new ResponseDTO<>(400, false, e.getMessage(), null);
        }

        return new ResponseDTO<>(200, true, "색깔을 불러왔습니다.", colors);
    }

    @GetMapping("/{user_id}/color")
    public ResponseDTO<Color> getDailyColor(@RequestHeader("Authorization") String accessToken,
                                            @PathVariable("user_id") Long userId,
                                            @RequestParam("date") String date) {
        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }

        try {
            userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMeaage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        LocalDate localDate = LocalDate.parse(date);
        Color color;

        try {
            color = userService.getDailyColor(userId, localDate);
        } catch (Exception e) {
            return new ResponseDTO<>(400, false, e.getMessage(), null);
        }

        return new ResponseDTO<>(200, true, "색깔을 불러왔습니다.", color);
    }

    // Diary 저장
    @PostMapping("/diary")
    public ResponseDTO<DiaryDTO> saveDiaryContents(@RequestHeader("Authorization") String accessToken,
                                                   @RequestBody SaveChatReqDTO requestbody){
        Long userId = requestbody.getUserId();
        String content = requestbody.getContent();
        DiaryDTO diaryDTO;

        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }
        try {
            diaryDTO = userService.saveDiaryContents(userId, content);
        } catch (Exception e) {
            System.out.println("e.getMassage : " + e.getMessage());
            return new ResponseDTO<>(400, false, "일기가 저장되지 않았습니다.", null);
        }

        return new ResponseDTO<>(200, true, "일기가 저장되었습니다.", diaryDTO);
    }

    // Diary 불러오기
    @GetMapping("/{user_id}/diary")
    public ResponseDTO<Diary> getDiary(@RequestHeader("Authorization") String accessToken,
                                       @PathVariable("user_id") Long userId,
                                       @RequestParam("date") String date) {
        if (jwtService.isExpired(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }
        try {
            userService.getUserByUserId(userId);
        } catch (Exception e) {
            System.out.println("e.getMessage() : " + e.getMessage());
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        LocalDate localDate = LocalDate.parse(date);
        Diary diary;

        try {
            diary = userService.getDiary(userId, localDate);
        } catch (Exception e) {
            return new ResponseDTO<>(400, false, e.getMessage(), null);
        }

        return new ResponseDTO<>(200, true, "일기를 불러왔습니다.", diary);
    }

}
