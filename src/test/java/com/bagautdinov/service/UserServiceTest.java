package com.bagautdinov.service;

import com.bagautdinov.config.properties.MailProperties;
import com.bagautdinov.dto.UserDto;
import com.bagautdinov.model.Role;
import com.bagautdinov.model.User;
import com.bagautdinov.repository.RoleRepository;
import com.bagautdinov.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MailProperties mailProperties;

    @InjectMocks
    private UserService userService;

    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setId(1L);
        userRole.setName("ROLE_USER");
    }

    @Test
    void findAllMapsUsersToDtos() {
        when(userRepository.findAllByOrderByIdAsc()).thenReturn(List.of(createUser(1L, "ivan")));

        List<UserDto> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("ivan", result.get(0).getUsername());
    }

    @Test
    void createSavesNewVerifiedUser() {
        when(userRepository.existsByUsername("ivan")).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("defaultPassword")).thenReturn("encoded-default");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(10L);
            return user;
        });

        UserDto result = userService.create("ivan");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("ivan", savedUser.getUsername());
        assertEquals("ivan@example.com", savedUser.getEmail());
        assertEquals("encoded-default", savedUser.getPassword());
        assertTrue(savedUser.isVerified());
        assertEquals(10L, result.getId());
    }

    @Test
    void createThrowsWhenUsernameAlreadyExists() {
        when(userRepository.existsByUsername("ivan")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.create("ivan"));

        assertEquals("User already exists with username: ivan", exception.getMessage());
    }

    @Test
    void registerSavesUserAndSendsVerificationMail() {
        MimeMessage mimeMessage = new MimeMessage((jakarta.mail.Session) null);

        when(userRepository.existsByUsername("ivan")).thenReturn(false);
        when(userRepository.existsByEmail("ivan@mail.com")).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
        when(mailProperties.getFrom()).thenReturn("test@gmail.com");
        when(mailProperties.getSender()).thenReturn("Test Application");
        when(mailProperties.getSubject()).thenReturn("Please verify your account");
        when(mailProperties.getContent()).thenReturn("Dear $name, follow $url");
        when(mailProperties.getBaseUrl()).thenReturn("http://localhost:8200");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(20L);
            return user;
        });

        UserDto result = userService.register("ivan", "ivan@mail.com", "secret");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("ivan", savedUser.getUsername());
        assertEquals("ivan@mail.com", savedUser.getEmail());
        assertEquals("encoded-secret", savedUser.getPassword());
        assertFalse(savedUser.isVerified());
        assertNotNull(savedUser.getVerificationCode());
        assertEquals(20L, result.getId());
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void verifyMarksUserAsVerified() {
        User user = createUser(30L, "vera");
        user.setVerified(false);
        user.setVerificationCode("code");
        when(userRepository.findByVerificationCode("code")).thenReturn(Optional.of(user));

        boolean result = userService.verify("code");

        assertTrue(result);
        assertTrue(user.isVerified());
        assertEquals(null, user.getVerificationCode());
        verify(userRepository).save(user);
    }

    @Test
    void verifyReturnsFalseWhenUserWasAlreadyVerified() {
        User user = createUser(31L, "vera");
        user.setVerified(true);
        user.setVerificationCode("code");
        when(userRepository.findByVerificationCode("code")).thenReturn(Optional.of(user));

        boolean result = userService.verify("code");

        assertFalse(result);
    }

    @Test
    void updateReturnsUpdatedDto() {
        User updatedUser = createUser(43L, "new-name");
        when(userRepository.existsById(43L)).thenReturn(true);
        when(userRepository.existsByUsername("new-name")).thenReturn(false);
        when(userRepository.updateUsernameById(43L, "new-name")).thenReturn(1);
        when(userRepository.findById(43L)).thenReturn(Optional.of(updatedUser));

        UserDto result = userService.update(43L, "new-name");

        assertEquals(43L, result.getId());
        assertEquals("new-name", result.getUsername());
    }

    @Test
    void updateThrowsWhenUsernameBelongsToDifferentUser() {
        User otherUser = createUser(50L, "taken");
        when(userRepository.existsById(41L)).thenReturn(true);
        when(userRepository.existsByUsername("taken")).thenReturn(true);
        when(userRepository.findByUsername("taken")).thenReturn(Optional.of(otherUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.update(41L, "taken"));

        assertEquals("Username already taken: taken", exception.getMessage());
    }

    @Test
    void deleteByIdDeletesExistingUser() {
        when(userRepository.existsById(60L)).thenReturn(true);

        userService.deleteById(60L);

        verify(userRepository).deleteById(60L);
    }

    @Test
    void deleteByUsernameDeletesExistingUser() {
        when(userRepository.existsByUsername("ivan")).thenReturn(true);

        userService.deleteByUsername("ivan");

        verify(userRepository).deleteByUsername("ivan");
    }

    private User createUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword("encoded");
        user.setEmail(username + "@mail.com");
        user.setVerified(true);
        user.setRoles(List.of(userRole));
        return user;
    }
}
