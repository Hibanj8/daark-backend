package com.daark.backend.controller;

import com.daark.backend.dto.UserDTO;
import com.daark.backend.service.UserService;
import com.daark.backend.dto.UpdateTelephoneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

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
    public ResponseEntity<String> updateTelephone(Authentication authentication, @RequestBody @Valid UpdateTelephoneRequest request) {
        String email = authentication.getName();
        userService.updateTelephone(email, request.getTelephone());
        return ResponseEntity.ok("Téléphone mis à jour avec succès.");
    }

}
