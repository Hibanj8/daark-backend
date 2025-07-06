package com.daark.backend.service;


import com.daark.backend.config.JwtUtils;
import com.daark.backend.dto.*;
import com.daark.backend.entity.Role;
import com.daark.backend.entity.User;
import com.daark.backend.exception.*;
import com.daark.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsedException();
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyUsedException();
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .telephone(request.getTelephone())
                .role(request.getRole() != null ? Role.valueOf(request.getRole().name()) : Role.CLIENT)
                .emailVerified(false)
                .build();

        userRepository.save(user);
        emailService.sendVerificationEmail(user);
    }


    public AuthResponse login(AuthRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(Exception::new);
            if (!user.isEmailVerified()) {
                throw new Exception();
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
            return new AuthResponse(token, user.getRole());


        } catch (Exception e) {
            throw new BadCredentialsException();
        }
    }


        public void verifyEmailToken(String token) {
        String email = jwtUtils.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setEmailVerified(true);
        userRepository.save(user);
    }
    public void sendResetPasswordEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email non trouvé"));
        String token = jwtUtils.generateVerificationToken(email); // Token court
        emailService.sendResetPasswordEmail(user, token);
    }
    public void resetPassword(NewPasswordRequest request) {
        String email = jwtUtils.extractEmail(request.getToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

}