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

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserDto findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserDto create(@RequestParam("username") String username) {
        return userService.create(username);
    }

    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserDto update(@PathVariable("id") Long id,
                          @RequestParam("username") String username) {
        return userService.update(id, username);
    }

    @PostMapping(value = "/{id}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "{\"message\":\"User deleted successfully\"}";
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

    @PostMapping("/update")
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam("username") String username) {
        userService.update(id, username);
        return "redirect:/users/page";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/users/page";
    }
}