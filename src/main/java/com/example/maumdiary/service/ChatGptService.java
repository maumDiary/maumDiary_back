package com.example.maumdiary.service;

import com.example.maumdiary.config.ChatGPTConfig;
import com.example.maumdiary.dto.ChatGptMessage;
import com.example.maumdiary.dto.ChatGptRequestDto;
import com.example.maumdiary.dto.ChatGptResponseDto;
import com.example.maumdiary.entity.Diary;
import com.example.maumdiary.entity.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGptService {

    private final static RestTemplate restTemplate = new RestTemplate();

    private final DiaryRepository diaryRepository;

    @Autowired
    public ChatGptService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    // chat gpt에게 보낼 질문에 header를 붙이는 메서드
    public static HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGPTConfig.MEDIA_TYPE));
        headers.add(ChatGPTConfig.AUTHORIZATION, ChatGPTConfig.BEARER + ChatGPTConfig.API_KEY);
        return new HttpEntity(requestDto, headers);
    }

    // chat gpt에게 응답을 받아오는 메서드
    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> chatGptRequestDtoHttpEntity) {
        // 답변이 길어질 경우 Timeout Error를 대비하여 2분으로 설정
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(120000);
        requestFactory.setReadTimeout(120*1000);
        restTemplate.setRequestFactory(requestFactory);

        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
                ChatGPTConfig.URL,
                chatGptRequestDtoHttpEntity,
                ChatGptResponseDto.class);

        return responseEntity.getBody();
    }

    // chat gpt에게 질문을 형식에 맞게 보내고 응답을 받아오는 메서드
    public ChatGptResponseDto askQuestion(String question) {
        List<ChatGptMessage> messages = new ArrayList<>();
        ChatGptMessage chatGptMessage = new ChatGptMessage(ChatGPTConfig.ROLE, question);
        messages.add(0,chatGptMessage);

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

    // chat gpt가 작성해준 일기를 db에 저장하는 메서드
    public void saveDiary(Long userId, String content) {
        LocalDate now = LocalDate.now();
        Diary diary = new Diary(userId, content, now);
        diaryRepository.save(diary);
    }

}
