package com.daark.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AnnonceRequest {
    @NotBlank
    private String ville;

    @NotBlank
    private String typeLogement;

    @NotBlank
    private String typeLocation;

    @NotBlank
    private String adresse;

    @NotBlank
    private String description;

    @NotNull
    private Double prix;

    @NotNull
    private Integer chambres;

    @NotNull
    private Integer salons;

    @NotNull
    private Integer sallesBain;

    @NotNull
    private Double superficie;

    @NotNull
    private Integer etage;

    @NotNull
    private Boolean meuble;

    @NotNull
    private Boolean nonFumeur;

    @NotNull
    private Boolean animaux;

    @NotNull
    private Boolean caution;

    @NotNull
    @Size(min = 3, max = 15)
    private List<MultipartFile> photos;


}
