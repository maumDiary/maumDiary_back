package com.example.maumdiary.service;

import com.example.maumdiary.dto.ChatDTO;
import com.example.maumdiary.entity.Chat;
import com.example.maumdiary.entity.ChatRepository;
import com.example.maumdiary.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    @Autowired
    public UserService(UserRepository userRepository, ChatRepository chatRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
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
            concatenatedContent.append(chat.getContent());
        }
        return concatenatedContent.toString();
    }

    public ChatDTO saveChatContent(Long userId, String content) {
        LocalDateTime datetime = LocalDateTime.now();
        Chat chat = new Chat(userId, content, datetime);
        chatRepository.save(chat);
        return new ChatDTO(userId, content, datetime);
    }

}
