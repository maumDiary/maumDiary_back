package com.example.maumdiary.controller;

import com.example.maumdiary.dto.JwtDTO;
import com.example.maumdiary.dto.ResponseDTO;
import com.example.maumdiary.entity.User;
import com.example.maumdiary.service.GoogleLoginService;
import com.example.maumdiary.service.JwtService;
import com.example.maumdiary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final JwtService jwtService;
    private final GoogleLoginService googleLoginService;

    // 구글 로그인
    @GetMapping("/google")
    public ResponseDTO<JwtDTO> googleLogin(@RequestHeader("Authorization") String token) throws GeneralSecurityException, IOException {
        String socialId = googleLoginService.getSocialIdByToken(token);
        // 유효하지 않은 토큰의 경우 socialId를 null로 리턴
        if (socialId == null) {
            return new ResponseDTO<>(401, false, "토큰이 유효하지 않습니다.", null);
        }

        // 디비에서 사용자 조회
        User user;
        if ((user = userService.getUserByGoogleId(socialId)) == null) {
            return new ResponseDTO<>(404, false, "사용자 정보가 없습니다.", null);
        }

        // 사용자가 있으면 JWT 자체 토큰 발급 후 로그인 완료 처리
        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken(user);

        JwtDTO jwtDTO = new JwtDTO(user.getId(), accessToken, refreshToken);
        return new ResponseDTO<>(200, true, "로그인이 완료되었습니다.", jwtDTO);
    }
}
