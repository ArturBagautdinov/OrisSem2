package com.bagautdinov.service;

import com.bagautdinov.model.User;
import com.bagautdinov.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameReturnsCustomUserDetails() {
        User user = new User();
        user.setUsername("ivan");
        user.setPassword("secret");
        when(userRepository.findByUsername("ivan")).thenReturn(Optional.of(user));

        Object result = customUserDetailsService.loadUserByUsername("ivan");

        assertInstanceOf(CustomUserDetails.class, result);
        assertEquals("ivan", ((CustomUserDetails) result).getUsername());
    }

    @Test
    void loadUserByUsernameThrowsWhenUserIsMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("ghost"));

        assertEquals("ghost", exception.getMessage());
    }
}
