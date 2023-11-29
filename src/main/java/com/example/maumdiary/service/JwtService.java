package com.example.maumdiary.service;

import com.example.maumdiary.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey SECRET_KEY;

    public JwtService() {
        // 생성자에서 키를 초기화
        this.SECRET_KEY = generatedSecretKey();
        System.out.println("generated Key : " + this.SECRET_KEY);
    }

    private SecretKey generatedSecretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public Map<String, Object> setTokenHeader() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg","HS256");
        headers.put("typ", "JWT");
        return headers;
    }

    public Map<String, Object> setTokenPayload(User user) {
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("id", user.getId());
        payloads.put("socialIdType", user.getSocialIdType());
        payloads.put("socialId", user.getSocialId());
        payloads.put("nickname", user.getNickname());
        payloads.put("exp", user.getExp());
        payloads.put("level", user.getLevel());

        return payloads;
    }

    // 액세스 토큰 생성
    public String createAccessToken(User user) {

        // header
        Map<String, Object> headers = setTokenHeader();

        //payload
        Map<String, Object> payloads = setTokenPayload(user);

        String accessToken = Jwts.builder()
                //header
                .setHeader(headers)
                .setSubject("accessToken")
                //payload
                .setClaims(payloads)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000*60*30))) // accessToken 유효기간 30분
                //signature
                .signWith(SECRET_KEY)
                .compact();

        return accessToken;
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(User user) {

        // header
        Map<String, Object> headers = setTokenHeader();

        //payload
        Map<String, Object> payloads = setTokenPayload(user);

        String refreshToken = Jwts.builder()
                //header
                .setHeader(headers)         // 토큰 타입
                .setSubject("refreshToken") // 토큰 제목
                //payload
                .setClaims(payloads)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000*60*60*24*14))) // refreshToken 유효기간 2주
                //signature
                .signWith(SECRET_KEY)
                .compact();

        return refreshToken;
    }

    // 토큰 만료 확인
    public Boolean isExpired(String token){
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            return expiration.before(now);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었을 경우
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // 그 외의 예외 처리 (서명 오류 등)
            e.printStackTrace();
            return false;
        }
    }

    // 리프레시 토큰이 3일 이하로 남았으면 true
    public Boolean isAlmostOver(String token){
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            long millisecondsInThreeDays = 3 * 24 * 60 * 60 * 1000; // 3일을 밀리초로 변환
            return expiration.getTime() - now.getTime() < millisecondsInThreeDays;
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            return false;
        } catch (Exception e) {
            // 그 외의 예외 처리 (서명 오류 등)
            e.printStackTrace();
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public User getPayload(String token) {

        Claims claims = getAllClaimsFromToken(token);
        User user = new User();
        user.setId(claims.get("userId", Long.class));
        user.setSocialId(claims.get("socialId", String.class));

        user.setSocialIdType(claims.get("socialIdType", String.class));

        user.setNickname(claims.get("nickname", String.class));
        user.setExp(claims.get("exp", Integer.class));
        user.setLevel(claims.get("userImage", Integer.class));
        return user;
    }

}
