package com.daark.backend.controller;

import com.daark.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class EmailVerificationController {
    private final AuthService authService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        authService.verifyEmailToken(token);
        return ResponseEntity.ok("Email vérifié avec succès.");
    }

}
