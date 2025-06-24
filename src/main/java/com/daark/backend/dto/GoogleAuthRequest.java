package com.daark.backend.dto;

import lombok.Data;

@Data
public class GoogleAuthRequest {
    private String token;
    private String type;
}
