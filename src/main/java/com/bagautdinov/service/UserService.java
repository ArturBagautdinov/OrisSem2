package com.bagautdinov.service;

import com.bagautdinov.dto.UserDto;
import com.bagautdinov.model.User;
import com.bagautdinov.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(u -> new UserDto(u.getId(), u.getUsername()))
                .toList();
    }

    @Transactional
    public UserDto create(String username) {
        User user = new User(username);
        userRepository.save(user);
        return new UserDto(user.getId(), user.getUsername());
    }
}
