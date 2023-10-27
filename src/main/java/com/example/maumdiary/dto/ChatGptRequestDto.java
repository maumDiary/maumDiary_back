package com.example.maumdiary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChatGptRequestDto implements Serializable {

    private String model;
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    private Double temperature;
    @JsonProperty("top_p")
    private Double topP;
    private List<ChatGptMessage> messages;

    @Builder
    public ChatGptRequestDto(String model, Integer maxTokens, Double temperature,
                             Double topP, List<ChatGptMessage> messages) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.topP = topP;
        this.messages = messages;
    }
}