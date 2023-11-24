package com.example.maumdiary.service;

import com.example.maumdiary.dto.ChatDTO;
import com.example.maumdiary.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    private final DiaryRepository diaryRepository;

    private final ColorRepository colorRepository;

    @Autowired
    public UserService(UserRepository userRepository, ChatRepository chatRepository, DiaryRepository diaryRepository, ColorRepository colorRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.diaryRepository = diaryRepository;
        this.colorRepository = colorRepository;
    }

    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).get();
    }

    public List<Chat> getChatDataByUserIdFromTodayMorning(Long userId) {
        LocalDateTime morning8AM = LocalDateTime.now().withHour(8).withMinute(0).withSecond(0);
        LocalDateTime currentTime = LocalDateTime.now();
        return chatRepository.findByUserIdAndDatetimeBetween(userId, morning8AM, currentTime);
    }

    // db에서 해당 날짜의 채팅 내용을 불러와서 연결하는 메서드
    public String concatChat(Long userId) {
        List<Chat> chatList = getChatDataByUserIdFromTodayMorning(userId);
        StringBuilder concatenatedContent = new StringBuilder();
        for (Chat chat : chatList) {
            concatenatedContent.append(chat.getContent() + " ");
        }
        return concatenatedContent.toString();
    }

    public ChatDTO saveChatContent(Long userId, String content) {
        LocalDateTime datetime = LocalDateTime.now();
        Chat chat = new Chat(userId, content, datetime);
        chatRepository.save(chat);
        return new ChatDTO(userId, content, datetime);
    }

    public Diary getDiary(Long userId, LocalDate date) {
        return diaryRepository.findByUserIdAndCreatedAt(userId, date);
    }

    public void updateLevel(Long userId, int level) {
        User user = userRepository.findById(userId).get();
        user.setLevel(level);
        userRepository.save(user);
    }

    public Color saveColor(Long userId, String colorName, LocalDate date) {
        Color color = new Color(userId, colorName, date);
        Color colorOfDate;
        // 사용자가 해당 날짜에 색깔을 선택하지 않았을 때
        if ((colorOfDate = colorRepository.findColorByUserIdAndDate(userId, date)) == null) {
            colorRepository.save(color);
        }
        // 사용자가 해당 날짜에 이미 색깔을 선택했을 때
        else {
            Long colorId = colorOfDate.getColorId();
            colorRepository.deleteById(colorId);
            colorRepository.save(color);
        }

        return colorRepository.findColorByUserIdAndDate(userId, date);
    }

    public List<Chat> getChats(Long userId) {
        return chatRepository.findChatsByUserId(userId);
    }

    public Color getColor(Long userId, LocalDate date) {
        return colorRepository.findColorByUserIdAndDate(userId, date);
    }

    public List<Color> getMonthlyColor(Long userId, Date month) {
        // 2. Date -> LocalDate
        LocalDate date = new java.sql.Date(month.getTime())  // java.util.Date -> java.sql.Date
                .toLocalDate();  // java.sql.Date -> LocalDate

        LocalDate startDate = date.withDayOfMonth(1);
        LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());

        return colorRepository.findColorsByUserIdAndDateBetween(userId, startDate, endDate);
    }

    public Color getDailyColor(Long userId, LocalDate date) {
        return colorRepository.findColorByUserIdAndDate(userId, date);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

}
