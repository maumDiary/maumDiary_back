package com.example.maumdiary.controller;

import com.example.maumdiary.dto.ResponseDTO;
import com.example.maumdiary.dto.UserDTO;
import com.example.maumdiary.entity.User;
import com.example.maumdiary.component.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class MeController {

    private final JwtProvider jwtProvider;

    @GetMapping
    public ResponseDTO<UserDTO> meAPI(@RequestHeader("Authorization") String accessToken) {
        if (!jwtProvider.verify(accessToken)) {
            return new ResponseDTO<>(401, false, "액세스 토큰이 만료되었습니다.", null);
        }

        User user;
        try {
            user = jwtProvider.getPayload(accessToken);
        } catch (Exception e) {
            System.out.println("e.getMessage() : " + e.getMessage());
            return new ResponseDTO<>(400, false, e.getMessage(), null);
        }

        UserDTO userDTO = new UserDTO(user.getId(), user.getNickname(), user.getLevel(), user.getExp());
        return new ResponseDTO<>(200, true, "사용자 정보를 가져왔습니다.", userDTO);
    }
}
