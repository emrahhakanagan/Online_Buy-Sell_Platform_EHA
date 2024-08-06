package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private String username;
    private User user;

    @BeforeEach
    public void setUpUser() {
        username = "test@example.com";
        user = new User();
        user.setEmail(username);
        user.setPassword("password");
    }

    @Test
    @DisplayName("Should load user by username when user exists and email is confirmed")
    public void testLoadUserByUsername_WhenUserExistsAndEmailConfirmed() {
        user.setConfirmed(true);
        when(userRepository.findByEmail(username)).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    public void testLoadUserByUsername_WhenUserDoesNotExist() {
        String username = "nonexistent@example.com";
        when(userRepository.findByEmail(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when email is not confirmed")
    public void testLoadUserByUsername_WhenEmailNotConfirmed() {
        user.setConfirmed(false);
        when(userRepository.findByEmail(username)).thenReturn(user);

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
    }
}

