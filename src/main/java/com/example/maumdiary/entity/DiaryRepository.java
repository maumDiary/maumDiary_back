package com.example.maumdiary.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Diary findDiaryByUserIdAndCreatedAt(Long userId, LocalDate createdAt);
}
