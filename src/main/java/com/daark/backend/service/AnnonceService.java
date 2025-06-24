package com.daark.backend.service;

import com.daark.backend.dto.AnnonceRequest;
import com.daark.backend.dto.AnnonceResponse;
import com.daark.backend.entity.Annonce;
import com.daark.backend.entity.User;
import com.daark.backend.repository.AnnonceRepository;
import com.daark.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AnnonceService {
    private final AnnonceRepository annonceRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public void createAnnonce(String email, AnnonceRequest request) throws IOException {
        User user = userRepository.findByEmail(email).orElseThrow();

        if (request.getPhotos().size() < 3 || request.getPhotos().size() > 15) {
            throw new IllegalArgumentException("Tu dois fournir entre 3 et 15 photos.");
        }

        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : request.getPhotos()) {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = Paths.get(uploadDir, filename).toFile();
            file.transferTo(dest);
            imageUrls.add("/uploads/" + filename);
        }

        Annonce annonce = Annonce.builder()
                .ville(request.getVille())
                .typeLogement(request.getTypeLogement())
                .typeLocation(request.getTypeLocation())
                .adresse(request.getAdresse())
                .description(request.getDescription())
                .prix(request.getPrix())
                .chambres(request.getChambres())
                .salons(request.getSalons())
                .sallesBain(request.getSallesBain())
                .superficie(request.getSuperficie())
                .etage(request.getEtage())
                .meuble(request.getMeuble())
                .nonFumeur(request.getNonFumeur())
                .animaux(request.getAnimaux())
                .caution(request.getCaution())
                .photos(imageUrls)
                .statut(Annonce.StatutAnnonce.EN_ATTENTE)
                .user(user)
                .build();

        annonceRepository.save(annonce);
    }
    @Transactional(readOnly = true)
    public List<AnnonceResponse> getAnnonces(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Annonce> annonces;

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            annonces = annonceRepository.findAll();
        } else {
            annonces = annonceRepository.findByUser(user);
        }

        return annonces.stream().map(this::mapToResponse).toList();
    }

    private AnnonceResponse mapToResponse(Annonce annonce) {
        AnnonceResponse response = new AnnonceResponse();
        response.setId(annonce.getId());
        response.setVille(annonce.getVille());
        response.setTypeLogement(annonce.getTypeLogement());
        response.setTypeLocation(annonce.getTypeLocation());
        response.setAdresse(annonce.getAdresse());
        response.setDescription(annonce.getDescription());
        response.setPrix(annonce.getPrix());
        response.setChambres(annonce.getChambres());
        response.setSalons(annonce.getSalons());
        response.setSallesBain(annonce.getSallesBain());
        response.setSuperficie(annonce.getSuperficie());
        response.setEtage(annonce.getEtage());
        response.setMeuble(annonce.getMeuble());
        response.setNonFumeur(annonce.getNonFumeur());
        response.setAnimaux(annonce.getAnimaux());
        response.setCaution(annonce.getCaution());
        response.setPhotos(new ArrayList<>(annonce.getPhotos()));
        response.setStatut(annonce.getStatut());
        return response;
    }
    public void updateStatutAnnonce(Long annonceId, Annonce.StatutAnnonce nouveauStatut) {
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new IllegalArgumentException("Annonce introuvable"));
        annonce.setStatut(nouveauStatut);
        annonceRepository.save(annonce);
    }
    public void deleteAnnonce(Long annonceId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new IllegalArgumentException("Annonce introuvable."));
        if (!"ADMIN".equalsIgnoreCase(user.getRole()) && !annonce.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Vous n’avez pas le droit de supprimer cette annonce.");
        }

        annonceRepository.delete(annonce);
    }
    @Transactional(readOnly = true)
    public List<AnnonceResponse> getAnnoncesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Annonce> annonces = annonceRepository.findByUser(user);
        return annonces.stream().map(this::mapToResponse).toList();
    }
    @Transactional(readOnly = true)
    public List<AnnonceResponse> getAllAnnonces() {
        List<Annonce> annonces = annonceRepository.findByStatut(Annonce.StatutAnnonce.ACCEPTEE); // ou filtre par statut
        return annonces.stream().map(this::mapToResponse).toList();
    }
    @Transactional(readOnly = true)
    public List<AnnonceResponse> getAllAnnoncesEnAttente() {
        List<Annonce> annonces = annonceRepository.findByStatut(Annonce.StatutAnnonce.EN_ATTENTE);
        return annonces.stream().map(this::mapToResponse).toList();
    }
    @Transactional(readOnly = true)
    public List<AnnonceResponse> getFilteredAnnonces(String ville, String typeLocation, String typeLogement, Double minPrix, Double maxPrix) {
        List<Annonce> annonces = annonceRepository.searchByFilters(ville, typeLocation, typeLogement, minPrix, maxPrix);
        return annonces.stream().map(this::mapToResponse).toList();
    }
    @Transactional(readOnly = true)
    public AnnonceResponse getAnnonceById(Long id) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Annonce introuvable"));
        return mapToResponse(annonce);
    }
    public void deleteAllAnnoncesForUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Annonce> annonces = annonceRepository.findByUser(user);
        annonceRepository.deleteAll(annonces);
    }

}

