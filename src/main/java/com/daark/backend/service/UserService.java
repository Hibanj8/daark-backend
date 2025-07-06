package com.daark.backend.service;

import com.daark.backend.dto.UserDTO;
import com.daark.backend.entity.Role;
import com.daark.backend.entity.User;
import com.daark.backend.exception.UserNotFoundException;
import com.daark.backend.mapper.UserMapper;
import com.daark.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));
        return UserMapper.toDTO(user);
    }

    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));
        userRepository.delete(user);
    }
    public List<UserDTO> getAllUsers() {
        return userRepository.findByRole(Role.CLIENT)
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public void updateTelephone(String email, String telephone) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));
        user.setTelephone(telephone);
        userRepository.save(user);
    }
}
