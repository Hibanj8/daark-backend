package com.daark.backend.service;

import com.daark.backend.dto.AnnonceRequest;
import com.daark.backend.entity.Annonce;
import com.daark.backend.entity.User;
import com.daark.backend.repository.AnnonceRepository;
import com.daark.backend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnnonceServiceTest {

    private AnnonceService annonceService;
    private AnnonceRepository annonceRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        annonceRepository = mock(AnnonceRepository.class);
        userRepository = mock(UserRepository.class);
        annonceService = new AnnonceService(annonceRepository, userRepository);

        // Injection manuelle de la valeur @Value("${app.upload.dir}")
        ReflectionTestUtils.setField(annonceService, "uploadDir", "uploads");

        // S'assurer que le dossier existe pour ne pas avoir d'erreur de transfert
        File uploadDir = new File("uploads");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Test
    public void testCreateAnnonce_WhenPhotosValide_ShouldSaveAnnonce() throws IOException {
        // Données d'entrée
        String email = "test@example.com";
        User fakeUser = new User();
        fakeUser.setId(1L);
        fakeUser.setEmail(email);

        AnnonceRequest request = new AnnonceRequest();
        request.setVille("Agadir");
        request.setTypeLogement("Appartement");
        request.setTypeLocation("Location");
        request.setAdresse("Rue xyz");
        request.setDescription("Très belle maison");
        request.setPrix(1000.0);
        request.setChambres(2);
        request.setSalons(1);
        request.setSallesBain(1);
        request.setSuperficie(80.0);
        request.setEtage(2);
        request.setMeuble(true);
        request.setNonFumeur(false);
        request.setAnimaux(true);
        request.setCaution(true);

        // Simuler 3 fichiers photo
        List<MockMultipartFile> photos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            photos.add(new MockMultipartFile("photo", "photo" + i + ".jpg", "image/jpeg", "fake image content".getBytes()));
        }
        request.setPhotos(new ArrayList<>(photos));

        // Comportement des mocks
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(fakeUser));

        // Appel de la méthode à tester
        annonceService.createAnnonce(email, request);

        // Vérification que la méthode save a été appelée
        verify(annonceRepository, times(1)).save(any(Annonce.class));
    }
}
