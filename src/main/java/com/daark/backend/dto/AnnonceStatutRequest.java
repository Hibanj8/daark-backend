package com.daark.backend.dto;

import com.daark.backend.entity.Annonce.StatutAnnonce;
import lombok.Data;

@Data
public class AnnonceStatutRequest {
    private StatutAnnonce statut;
}