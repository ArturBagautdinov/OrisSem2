package com.bagautdinov.controller;

import com.bagautdinov.dto.UserDto;
import com.bagautdinov.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserDto create(@RequestParam("username") String username) {
        return userService.create(username);
    }

    @GetMapping("/page")
    public String usersPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }
    
    @PostMapping("/create")
    public String createUser(@RequestParam("username") String username) {
        userService.create(username);
        return "redirect:/users/page";
    }
}