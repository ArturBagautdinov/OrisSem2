package com.bagautdinov.controller;

import com.bagautdinov.dto.UserDto;
import com.bagautdinov.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void findAllReturnsUsersFromService() {
        List<UserDto> users = List.of(new UserDto(1L, "Ivan"));
        when(userService.findAll()).thenReturn(users);

        assertEquals(users, userController.findAll());
    }

    @Test
    void findByIdReturnsUserFromService() {
        UserDto user = new UserDto(2L, "Petr");
        when(userService.findById(2L)).thenReturn(user);

        assertEquals(user, userController.findById(2L));
    }

    @Test
    void createReturnsCreatedUser() {
        UserDto createdUser = new UserDto(3L, "Alex");
        when(userService.create("Alex")).thenReturn(createdUser);

        assertEquals(createdUser, userController.create("Alex"));
    }

    @Test
    void updateReturnsUpdatedUser() {
        UserDto updatedUser = new UserDto(4L, "Mila");
        when(userService.update(4L, "Mila")).thenReturn(updatedUser);

        assertEquals(updatedUser, userController.update(4L, "Mila"));
    }

    @Test
    void deleteDelegatesToServiceAndReturnsMessage() {
        assertEquals("{\"message\":\"User deleted successfully\"}", userController.delete(5L));
        verify(userService).deleteById(5L);
    }

    @Test
    void usersPageAddsUsersToModel() {
        List<UserDto> users = List.of(new UserDto(1L, "Ivan"));
        ExtendedModelMap model = new ExtendedModelMap();
        when(userService.findAll()).thenReturn(users);

        String viewName = userController.usersPage(model);

        assertEquals("users", viewName);
        assertEquals(users, model.get("users"));
    }

    @Test
    void createUserRedirectsToUsersPage() {
        assertEquals("redirect:/users/page", userController.createUser("Nina"));
        verify(userService).create("Nina");
    }

    @Test
    void updateUserRedirectsToUsersPage() {
        assertEquals("redirect:/users/page", userController.updateUser(10L, "Dina"));
        verify(userService).update(10L, "Dina");
    }

    @Test
    void deleteUserRedirectsToUsersPage() {
        assertEquals("redirect:/users/page", userController.deleteUser(11L));
        verify(userService).deleteById(11L);
    }
}
