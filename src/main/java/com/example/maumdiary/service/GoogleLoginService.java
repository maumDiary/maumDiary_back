package com.example.maumdiary.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleLoginService {

    @Value("${google.client.id}")
    private String CLIENT_ID;

    public String getSocialIdByToken(String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        System.out.println(CLIENT_ID);

        GoogleIdToken idToken = verifier.verify(token);
        System.out.println(idToken);
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            String socialId = payload.getSubject();
            System.out.println("socialId : " + socialId);
            return socialId;
        } else {
            System.out.println("Invalid Id token.");
            return null;
        }
    }
}
