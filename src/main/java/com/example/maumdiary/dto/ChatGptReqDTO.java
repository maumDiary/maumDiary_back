package com.example.maumdiary.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ChatGptReqDTO implements Serializable{
    String question;
}
