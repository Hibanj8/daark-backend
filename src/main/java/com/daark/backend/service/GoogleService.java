package com.daark.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleService {
    private final GoogleIdTokenVerifier verifier;

    public GoogleService() {
        verifier = new GoogleIdTokenVerifier.Builder(Utils.getDefaultTransport(), Utils.getDefaultJsonFactory())
                .setAudience(Collections.singletonList("407977946921-vcj7b9cp49qttibgmimdik92ad4ml7n6.apps.googleusercontent.com")) // remplace avec ton ID client Google
                .build();
    }

    public GoogleIdToken.Payload verifyToken(String token) {
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                return idToken.getPayload();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
