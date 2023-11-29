package com.example.maumdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtDTO {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
