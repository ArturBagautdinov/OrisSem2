package com.bagautdinov.service;

import com.bagautdinov.config.properties.MailProperties;
import com.bagautdinov.dto.UserDto;
import com.bagautdinov.model.Role;
import com.bagautdinov.model.User;
import com.bagautdinov.repository.RoleRepository;
import com.bagautdinov.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JavaMailSender mailSender,
                       MailProperties mailProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
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

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role ROLE_USER not found"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPassword(passwordEncoder.encode("defaultPassword"));
        user.setVerified(true);
        user.setRoles(List.of(userRole));

        User savedUser = userRepository.save(user);
        return new UserDto(savedUser.getId(), savedUser.getUsername());
    }

    public UserDto register(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Пользователь с такой почтой уже существует");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role ROLE_USER not found"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(List.of(userRole));
        user.setVerified(false);
        user.setVerificationCode(UUID.randomUUID().toString());

        User savedUser = userRepository.save(user);
        sendVerificationMail(savedUser);
        return new UserDto(savedUser.getId(), savedUser.getUsername());
    }

    public boolean verify(String code) {
        User user = userRepository.findByVerificationCode(code)
                .orElseThrow(() -> new RuntimeException("Некорректный код подтверждения"));

        if (user.isVerified()) {
            return false;
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);
        return true;
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

    private void sendVerificationMail(User user) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(mailProperties.getFrom(), mailProperties.getSender());
            helper.setTo(user.getEmail());
            helper.setSubject(mailProperties.getSubject());

            String content = mailProperties.getContent()
                    .replace("$name", user.getUsername())
                    .replace("$url", mailProperties.getBaseUrl() + "/verification?code=" + user.getVerificationCode());

            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Не удалось отправить письмо с подтверждением", e);
        }
    }
}
