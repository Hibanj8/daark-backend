package com.daark.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class UpdateTelephoneRequest {
    @NotBlank(message = "Le numéro de téléphone est requis")
    @Pattern(regexp = "\\d{9}", message = "Le numéro de téléphone doit contenir exactement 9 chiffres")
    private String telephone;

}