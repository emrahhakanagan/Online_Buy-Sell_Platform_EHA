package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.model.enums.Role;
import com.agan.onlinebuysellplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterNewUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setConfirmed(false);

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.registerNewUser("Test User", "1234567890", "test@example.com", "password");

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void testConfirmUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setConfirmationToken("test-token");
        user.setConfirmed(false);

        when(userRepository.findByConfirmationToken("test-token")).thenReturn(user);

        userService.confirmUser("test-token");

        verify(userRepository, times(1)).findByConfirmationToken("test-token");
        verify(userRepository, times(1)).save(user);
        assertTrue(user.isConfirmed());
    }

    @Test
    public void testConfirmUserWithInvalidToken() {
        when(userRepository.findByConfirmationToken("invalid-token")).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.confirmUser("invalid-token");
        });

        String expectedMessage = "Invalid token";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(userRepository, times(1)).findByConfirmationToken("invalid-token");
        verify(userRepository, times(0)).save(any(User.class));
    }
}
