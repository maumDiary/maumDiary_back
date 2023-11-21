package com.example.maumdiary.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "social_id_type", nullable = false)
    private String socialIdType;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "connect_num", nullable = false)
    private int connectNum;

    @Builder
    public User(String socialIdType, String socialId, String nickname, int level, int connectNum) {
        this.socialIdType = socialIdType;
        this.socialId = socialId;
        this.nickname = nickname;
        this.level = level;
        this.connectNum = connectNum;
    }
}
