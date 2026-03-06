package com.bagautdinov.service;

import com.bagautdinov.dto.UserDto;
import com.bagautdinov.model.User;
import com.bagautdinov.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAllByOrderByIdAsc().stream()
                .map(user -> new UserDto(user.getId(), user.getUsername()))
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return new UserDto(user.getId(), user.getUsername());
    }

    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        return new UserDto(user.getId(), user.getUsername());
    }

    public UserDto create(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("User already exists with username: " + username);
        }

        User savedUser = userRepository.save(new User(username));
        return new UserDto(savedUser.getId(), savedUser.getUsername());
    }

    public UserDto update(Long id, String username) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        if (userRepository.existsByUsername(username)) {
            User existingUser = userRepository.findByUsername(username).orElse(null);
            if (existingUser != null && !existingUser.getId().equals(id)) {
                throw new RuntimeException("Username already taken: " + username);
            }
        }

        int updatedRows = userRepository.updateUsernameById(id, username);

        if (updatedRows == 0) {
            throw new RuntimeException("Failed to update user with id: " + id);
        }

        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return new UserDto(updatedUser.getId(), updatedUser.getUsername());
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    public void deleteByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new RuntimeException("User not found with username: " + username);
        }

        userRepository.deleteByUsername(username);
    }
}