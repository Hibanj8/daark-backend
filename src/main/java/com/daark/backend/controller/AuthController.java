package com.daark.backend.controller;

import com.daark.backend.dto.AuthRequest;
import com.daark.backend.dto.AuthResponse;
import com.daark.backend.dto.RegisterRequest;
import com.daark.backend.repository.UserRepository;
import com.daark.backend.service.AuthService;
import com.daark.backend.dto.ResetPasswordRequest;
import com.daark.backend.dto.NewPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.daark.backend.dto.GoogleAuthRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.daark.backend.service.GoogleService;
import com.daark.backend.entity.User;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.daark.backend.config.JwtUtils;


import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService;
    private final GoogleService googleService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

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

    @PostMapping("/google")
    public ResponseEntity<?> googleAuth(@RequestBody GoogleAuthRequest request) {
        String token = request.getToken();
        String type = request.getType();

        GoogleIdToken.Payload payload = googleService.verifyToken(token);
        if (payload == null) {
            return ResponseEntity.badRequest().body("Token Google invalide.");
        }

        String email = payload.getEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (type.equals("login") && userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Aucun compte trouvé.");
        }

        User user = userOpt.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername((String) payload.get("name"));
            newUser.setPassword(UUID.randomUUID().toString());
            return userRepository.save(newUser);
        });

        String jwt = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(Map.of("token", jwt));

    }


}
