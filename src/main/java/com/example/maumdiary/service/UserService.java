package com.example.maumdiary.service;

import com.example.maumdiary.dto.ChatDTO;
import com.example.maumdiary.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    private final DiaryRepository diaryRepository;

    @Autowired
    public UserService(UserRepository userRepository, ChatRepository chatRepository, DiaryRepository diaryRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.diaryRepository = diaryRepository;
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

}
