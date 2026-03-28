package com.bagautdinov.controller;

import com.bagautdinov.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Test endpoint works!";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           Model model) {
        try {
            userService.register(username, email, password);
            model.addAttribute("success", "Регистрация успешна. Проверьте почту и перейдите по ссылке для подтверждения аккаунта.");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/verification")
    public String verify(@RequestParam("code") String code, Model model) {
        try {
            boolean verified = userService.verify(code);
            if (verified) {
                model.addAttribute("success", "Почта успешно подтверждена. Теперь вы можете войти.");
            } else {
                model.addAttribute("success", "Почта уже была подтверждена ранее. Теперь вы можете войти.");
            }
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
}
