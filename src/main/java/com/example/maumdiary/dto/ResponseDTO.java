package com.example.maumdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDTO<T> {
    private final int status;
    private final boolean isSuccess;
    private final String message;
    private final T data;
}
