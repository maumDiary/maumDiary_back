package com.example.maumdiary.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "color")
public class Color {

    @Id
    @Column(name = "color_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long colorId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "color_name", nullable = false)
    private String colorName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Builder
    public Color(Long userId, String colorName, LocalDate date) {
        this.userId = userId;
        this.colorName = colorName;
        this.date = date;
    }
}
