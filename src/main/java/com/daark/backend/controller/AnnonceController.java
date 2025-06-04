package com.daark.backend.controller;

import com.daark.backend.dto.AnnonceRequest;
import com.daark.backend.dto.AnnonceResponse;
import com.daark.backend.dto.AnnonceStatutRequest;
import com.daark.backend.entity.User;
import com.daark.backend.repository.UserRepository;
import com.daark.backend.service.AnnonceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/annonces")
@RequiredArgsConstructor

public class AnnonceController {
    private final AnnonceService annonceService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createAnnonce(Authentication authentication,
                                                @Valid @ModelAttribute AnnonceRequest request) throws IOException {
        String email = authentication.getName();
        annonceService.createAnnonce(email, request);
        return ResponseEntity.ok("Annonce créée avec succès.");
    }

    @GetMapping
    public ResponseEntity<List<AnnonceResponse>> getAnnonces(Authentication authentication) {
        String email = authentication.getName();
        List<AnnonceResponse> annonces = annonceService.getAnnonces(email);
        return ResponseEntity.ok(annonces);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/en-attente")
    public ResponseEntity<List<AnnonceResponse>> getAnnoncesEnAttenteAdmin() {
        List<AnnonceResponse> annonces = annonceService.getAllAnnoncesEnAttente();
        return ResponseEntity.ok(annonces);
    }

    @PutMapping("/statut/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateStatut(@PathVariable Long id,
                                               @RequestBody AnnonceStatutRequest request) {
        annonceService.updateStatutAnnonce(id, request.getStatut());
        return ResponseEntity.ok("Statut mis à jour avec succès.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnnonce(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        try {
            annonceService.deleteAnnonce(id, email);
            return ResponseEntity.ok("Annonce supprimée avec succès.");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Accès refusé : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAnnoncesByUserId(@PathVariable Long userId) {
        List<AnnonceResponse> annonces = annonceService.getAnnoncesByUserId(userId);
        return ResponseEntity.ok(annonces);
    }

    @GetMapping("/public")
    public ResponseEntity<List<AnnonceResponse>> getAllAnnoncesPublic(
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) String typeLocation,
            @RequestParam(required = false) String typeLogement,
            @RequestParam(required = false) Double minPrix,
            @RequestParam(required = false) Double maxPrix
    ) {
        List<AnnonceResponse> annonces = annonceService.getFilteredAnnonces(ville, typeLocation, typeLogement, minPrix, maxPrix);
        return ResponseEntity.ok(annonces);
    }
}
