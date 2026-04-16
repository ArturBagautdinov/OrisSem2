package com.bagautdinov.controller;

import com.bagautdinov.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testEndpointReturnsExpectedMessage() {
        assertEquals("Test endpoint works!", authController.test());
    }

    @Test
    void loginPageReturnsLoginView() {
        assertEquals("login", authController.loginPage());
    }

    @Test
    void registerPageReturnsRegisterView() {
        assertEquals("register", authController.registerPage());
    }

    @Test
    void registerReturnsLoginOnSuccess() {
        ExtendedModelMap model = new ExtendedModelMap();

        String viewName = authController.register("ivan", "ivan@mail.com", "secret", model);

        assertEquals("login", viewName);
        assertEquals("Регистрация успешна. Проверьте почту и перейдите по ссылке для подтверждения аккаунта.", model.get("success"));
        verify(userService).register("ivan", "ivan@mail.com", "secret");
    }

    @Test
    void registerReturnsRegisterViewOnFailure() {
        ExtendedModelMap model = new ExtendedModelMap();
        doThrow(new RuntimeException("Ошибка регистрации"))
                .when(userService).register("ivan", "ivan@mail.com", "secret");

        String viewName = authController.register("ivan", "ivan@mail.com", "secret", model);

        assertEquals("register", viewName);
        assertEquals("Ошибка регистрации", model.get("error"));
    }

    @Test
    void verifyReturnsLoginWhenCodeIsAccepted() {
        ExtendedModelMap model = new ExtendedModelMap();
        when(userService.verify("code-1")).thenReturn(true);

        String viewName = authController.verify("code-1", model);

        assertEquals("login", viewName);
        assertEquals("Почта успешно подтверждена. Теперь вы можете войти.", model.get("success"));
    }

    @Test
    void verifyReturnsLoginWithSuccessMessageForRepeatedVerification() {
        ExtendedModelMap model = new ExtendedModelMap();
        when(userService.verify("code-2")).thenReturn(false);

        String viewName = authController.verify("code-2", model);

        assertEquals("login", viewName);
        assertEquals("Почта уже была подтверждена ранее. Теперь вы можете войти.", model.get("success"));
    }

    @Test
    void verifyReturnsErrorWhenServiceThrowsException() {
        ExtendedModelMap model = new ExtendedModelMap();
        when(userService.verify("bad-code")).thenThrow(new RuntimeException("Некорректный код"));

        String viewName = authController.verify("bad-code", model);

        assertEquals("login", viewName);
        assertEquals("Некорректный код", model.get("error"));
    }
}
