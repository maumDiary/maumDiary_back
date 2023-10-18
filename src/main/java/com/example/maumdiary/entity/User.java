package com.example.maumdiary.entity;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "social_id_type", nullable = false)
    private String socialIdType;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "level", nullable = false)
    private int level;

    @Builder
    public User(String socialIdType, String socialId, String nickname, int level) {
        this.socialIdType = socialIdType;
        this.socialId = socialId;
        this.nickname = nickname;
        this.level = level;
    }
}
