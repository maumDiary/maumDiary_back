package com.example.maumdiary.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponseDTO<T> {
    private final int code;
    private final boolean isSuccess;
    private final String message;
    private final T data;
}
