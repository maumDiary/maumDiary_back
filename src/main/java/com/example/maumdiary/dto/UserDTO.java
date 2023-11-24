package com.example.maumdiary.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDTO {
    private final Long userId;
    private final String nickname;
    private final int level;
    private final int exp;
}
