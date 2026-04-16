package com.bagautdinov.service;

import com.bagautdinov.model.Role;
import com.bagautdinov.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomUserDetailsTest {

    @Test
    void exposesUserCredentialsAndAuthorities() {
        Role role = new Role();
        role.setName("ROLE_USER");

        User user = new User();
        user.setUsername("ivan");
        user.setPassword("encoded");
        user.setVerified(true);
        user.setRoles(List.of(role));

        CustomUserDetails details = new CustomUserDetails(user);

        List<String> authorities = details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertEquals(List.of("ROLE_USER"), authorities);
        assertEquals("ivan", details.getUsername());
        assertEquals("encoded", details.getPassword());
        assertTrue(details.isEnabled());
    }

    @Test
    void isEnabledReturnsFalseForUnverifiedUsers() {
        User user = new User();
        user.setVerified(false);

        CustomUserDetails details = new CustomUserDetails(user);

        assertFalse(details.isEnabled());
    }
}
