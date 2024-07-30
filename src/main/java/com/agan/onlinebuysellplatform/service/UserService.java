package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.exception.UserNotFoundException;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.model.enums.Role;
import com.agan.onlinebuysellplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

//    public boolean createUser(User user) {
//        String email = user.getEmail();
//        if (userRepository.findByEmail(email) != null) return false;
//        user.setActive(true);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.getRoles().add(Role.ROLE_USER);
//        log.info("Saving new User with email: {}", email);
//        userRepository.save(user);
//        return true;
//    }

    public void registerNewUser(String username, String password, String email) throws Exception {
        if (userRepository.findByEmail(username) != null) {
            throw new Exception("User already exists");
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setConfirmationToken(UUID.randomUUID().toString());
        userRepository.save(user);

        // Sending email to User
        String subject = "Registration Confirmation";
        String text = "To confirm your registration, please click the next link:  http://localhost:8080/confirm?token=" + user.getConfirmationToken();
        emailService.sendEmail(email, subject, text);
    }

    public void confirmUser(String token) throws Exception {
        User user = userRepository.findByConfirmationToken(token);
        if (user == null) {
            throw new Exception("Invalid token");
        }
        user.setConfirmed(true);
        userRepository.save(user);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}; email: {}", user.getId(), user.getEmail());
            } else {
                user.setActive(true);
                log.info("Unban user with id = {}; email: {}", user.getId(), user.getEmail());
            }
        }
        userRepository.save(user);
    }

    public void changeUserRoles(Long userId, String[] roles) {

            Optional<User> optionalUser = Optional.ofNullable(userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found")));

            User user = optionalUser.get();

            Set<String> rolesFromEnum = Arrays.stream(Role.values())
                    .map(Role::name)
                    .collect(Collectors.toSet());

            user.getRoles().clear();

            Arrays.stream(roles)
                    .filter(rolesFromEnum::contains)
                    .map(Role::valueOf)
                    .forEach(user.getRoles()::add);

            userRepository.save(user);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
}