package com.daark.backend.dto;

import lombok.Data;

@Data
public class EmailVerificationRequest {
    private String email;
    private String verificationCode;
}
