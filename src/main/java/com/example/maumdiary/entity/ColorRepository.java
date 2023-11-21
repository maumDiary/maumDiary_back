package com.example.maumdiary.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Color findColorByUserIdAndDate(Long userId, LocalDate date);
}
