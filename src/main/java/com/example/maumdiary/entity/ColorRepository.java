package com.example.maumdiary.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Color findColorByUserIdAndDate(Long userId, LocalDate date);

    List<Color> findColorsByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
