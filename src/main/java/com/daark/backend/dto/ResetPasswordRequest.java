package com.daark.backend.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
}