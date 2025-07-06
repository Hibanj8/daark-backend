package com.daark.backend.dto;
import com.daark.backend.entity.StatutAnnonce;
import lombok.Data;
import java.util.List;

@Data
public class AnnonceResponse {
    private Long id;
    private String ville;
    private String typeLogement;
    private String typeLocation;
    private String adresse;
    private String description;
    private Double prix;
    private Integer chambres;
    private Integer salons;
    private Integer sallesBain;
    private Double superficie;
    private Integer etage;
    private Boolean meuble;
    private Boolean nonFumeur;
    private Boolean animaux;
    private Boolean caution;
    private List<String> photos;
    private StatutAnnonce statut;
    private Long userId;
    private String username;
    private String email;
    private String telephone;

}

