package com.example.maumdiary.component;

import com.example.maumdiary.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${custom.jwt.secretKey}")
    private String secretKeyPlain;

    private Key createKey() {
        // signiture에 대한 정보는 Byte array로 구성되어있습니다.
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKeyPlain);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
        return signingKey;
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
        payloads.put("userExp", user.getExp());
        payloads.put("level", user.getLevel());

        return payloads;
    }

    // 액세스 토큰 생성
    public String createAccessToken(User user) {

        // header
        Map<String, Object> headers = setTokenHeader();

        //payload
        Map<String, Object> payloads = setTokenPayload(user);

        return Jwts.builder()
                //header
                .setHeader(headers)
                .setSubject("accessToken")
                //payload
                .setClaims(payloads)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000*60*30)))
                //signature
                .signWith(createKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(User user) {

        // header
        Map<String, Object> headers = setTokenHeader();

        //payload
        Map<String, Object> payloads = setTokenPayload(user);

        return Jwts.builder()
                //header
                .setHeader(headers)
                .setSubject("refreshToken")
                //payload
                .setClaims(payloads)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000*60*60*24*14)))
                //signature
                .signWith(createKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        if (verify(token)) {
            return Jwts.parserBuilder()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKeyPlain))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } else {
            return null;
        }
    }

    public User getPayload(String token) {

        Claims claims = getAllClaimsFromToken(token);
        User user = new User();
        user.setId(Long.parseLong(claims.get("id").toString()));
        user.setSocialId(claims.get("socialId").toString());

        user.setSocialIdType(claims.get("socialIdType").toString());

        user.setNickname(claims.get("nickname").toString());
        user.setExp(Integer.parseInt(claims.get("userExp").toString()));
        user.setLevel(Integer.parseInt(claims.get("level").toString()));
        return user;
    }

    //-- Token 유효성 검사 --//
    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(createKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
