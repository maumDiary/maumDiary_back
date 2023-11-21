package com.example.maumdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ColorReqDTO {
    private final String color_name;
    private final LocalDate date;
}
