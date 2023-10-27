package com.example.maumdiary.service;

import com.example.maumdiary.config.ChatGPTConfig;
import com.example.maumdiary.dto.ChatGptMessage;
import com.example.maumdiary.dto.ChatGptRequestDto;
import com.example.maumdiary.dto.ChatGptResponseDto;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGptService {

    private static RestTemplate restTemplate = new RestTemplate();

    public static HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGPTConfig.MEDIA_TYPE));
        headers.add(ChatGPTConfig.AUTHORIZATION, ChatGPTConfig.BEARER + ChatGPTConfig.API_KEY);
        return new HttpEntity(requestDto, headers);
    }

    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> chatGptRequestDtoHttpEntity) {
        // 답변이 길어질 경우 Timeout Error를 대비하여 1분으로 설정
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60000);
        requestFactory.setReadTimeout(60*1000);
        restTemplate.setRequestFactory(requestFactory);

        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
                ChatGPTConfig.URL,
                chatGptRequestDtoHttpEntity,
                ChatGptResponseDto.class);

        return responseEntity.getBody();
    }

    public ChatGptResponseDto askQuestion(String question) {
        List<ChatGptMessage> messages = new ArrayList<>();
        messages.add(ChatGptMessage.builder()
                .role(ChatGPTConfig.ROLE)
                .content(question)
                .build());

        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                ChatGPTConfig.MODEL,
                                ChatGPTConfig.MAX_TOKEN,
                                ChatGPTConfig.TEMPERATURE,
                                ChatGPTConfig.TOP_P,
                                messages
                        )
                )
        );
    }
}
