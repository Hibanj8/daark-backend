package com.daark.backend.controller;

import com.daark.backend.dto.UserDTO;
import com.daark.backend.dto.UpdateTelephoneRequest;
import com.daark.backend.entity.User;
import com.daark.backend.service.UserService;
import com.daark.backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getUser(email));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteAccount(Authentication authentication) {
        String email = authentication.getName();
        userService.deleteAccount(email);
        return ResponseEntity.ok("Compte supprimé avec succès.");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/me/telephone")
    public ResponseEntity<String> updateTelephone(Authentication authentication,
                                                  @RequestBody @Valid UpdateTelephoneRequest request) {
        String email = authentication.getName();
        userService.updateTelephone(email, request.getTelephone());
        return ResponseEntity.ok("Téléphone mis à jour avec succès.");
    }

    @DeleteMapping("/admin/delete-user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserByAdmin(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(userId); // Suppression en cascade si bien configurée
        return ResponseEntity.ok("Utilisateur et ses annonces supprimés avec succès.");
    }

}