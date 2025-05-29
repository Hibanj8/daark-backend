package com.daark.backend.controller;

import com.daark.backend.dto.AuthRequest;
import com.daark.backend.dto.AuthResponse;
import com.daark.backend.dto.RegisterRequest;
import com.daark.backend.service.AuthService;
import com.daark.backend.dto.ResetPasswordRequest;
import com.daark.backend.dto.NewPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Inscription réussie. Vérifiez votre email.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody ResetPasswordRequest request) {
        authService.sendResetPasswordEmail(request.getEmail());
        return ResponseEntity.ok("Lien de réinitialisation envoyé à votre email.");
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> confirmResetPassword(@RequestBody @Valid NewPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Mot de passe mis à jour avec succès.");
    }

}
