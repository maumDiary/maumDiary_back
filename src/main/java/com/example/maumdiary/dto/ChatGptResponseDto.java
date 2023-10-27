package com.example.maumdiary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatGptResponseDto implements Serializable {

    private String id;
    private String object;
    private LocalDate created;
    private String model;
    private Usage usage;
    private List<Choice> choices;

    @Getter
    @Setter
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }

    @Getter
    @Setter
    public static class Choice {
        private int index;
        private List<ChatGptMessage> messages;
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @Builder
    public ChatGptResponseDto(String id, String object,
                              LocalDate created, String model,Usage usage,
                              List<Choice> choices) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.usage = usage;
        this.choices = choices;
    }

}