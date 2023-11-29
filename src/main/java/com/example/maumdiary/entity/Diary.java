package com.example.maumdiary.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "diary")
public class Diary implements Serializable {
    @Id
    @Column(name = "diary_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long diaryId;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column
    public String content;

    @Column(name = "created_at", nullable = false)
    public LocalDate createdAt;

    @Builder
    public Diary(Long userId, String content, LocalDate createdAt) {
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }


}
