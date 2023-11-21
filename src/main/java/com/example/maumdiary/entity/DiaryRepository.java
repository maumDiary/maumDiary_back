package com.example.maumdiary.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Diary findByUserIdAndCreatedAt(Long userId, LocalDate date);
}
