package com.agan.onlinebuysellplatform.controller;

import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Principal principal,
                        Model model) {

        if (principal != null) {
            model.addAttribute("user", userService.getUserByPrincipal(principal));
        } else {
            model.addAttribute("user", null);
        }

        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }

        return "login";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);

        return "profile";
    }

    @GetMapping("/registration")
    public String registration(Principal principal, Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }


    @PostMapping("/registration")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "registration";
        }

        try {
            userService.registerNewUser(user);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "registration";
        }
    }

    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam("token") String token, Model model) {
        try {
            User user = userService.confirmUser(token);
            model.addAttribute("user", user);
            return "confirm";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "confirm";
        }
    }

    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model, Principal principal) {
        model.addAttribute("user", user);
        model.addAttribute("userByPrincipal", userService.getUserByPrincipal(principal));
        model.addAttribute("products", user.getProducts());

        return "user-info";
    }
}
