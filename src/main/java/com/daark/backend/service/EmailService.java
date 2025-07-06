package com.daark.backend.service;

import com.daark.backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.daark.backend.config.JwtUtils;
import org.springframework.beans.factory.annotation.Value;


@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String senderEmail;
    private final JavaMailSender mailSender;
    private final JwtUtils jwtUtils;

    public void sendVerificationEmail(User user) {
        String token = jwtUtils.generateVerificationToken(user.getEmail());
        String frontendUrl = "http://localhost:5175/confirm-email?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(user.getEmail());
        message.setSubject("Vérification d'email");
        message.setText("Cliquez sur ce lien pour vérifier votre email :\n" + frontendUrl);

        mailSender.send(message);
    }
    public void sendResetPasswordEmail(User user, String token) {
        String link = "http://localhost:5175/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(user.getEmail());
        message.setSubject("Réinitialisation du mot de passe");
        message.setText("Cliquez ici pour réinitialiser votre mot de passe : " + link);
        mailSender.send(message);
    }

}
