package com.daark.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "annonces")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Annonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAnnonce statut;

    @ElementCollection
    @CollectionTable(name = "annonce_photos", joinColumns = @JoinColumn(name = "annonce_id"))
    @Column(name = "photo_url")
    private List<String> photos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    public void addPhoto(String photoUrl) {
        this.photos.add(photoUrl);
    }



}
