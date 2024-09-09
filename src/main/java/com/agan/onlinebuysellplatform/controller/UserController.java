package com.agan.onlinebuysellplatform.controller;

import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.service.UserService;
import com.agan.onlinebuysellplatform.validation.FormValidationGroup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
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
    public String registration(Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createUser(@Validated(FormValidationGroup.class) @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Unknown error")
                    ));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            userService.registerNewUser(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
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
