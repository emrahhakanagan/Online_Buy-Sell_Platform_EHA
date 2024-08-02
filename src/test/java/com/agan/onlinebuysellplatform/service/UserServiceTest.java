package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.exception.UserNotFoundException;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.model.enums.Role;
import com.agan.onlinebuysellplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

    private User user;

    @BeforeEach
    void setupMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUpUser() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setConfirmed(false);
    }

    @Test
    public void testRegisterNewUser() throws Exception {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        userService.registerNewUser(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(Collections.singleton(Role.ROLE_USER), savedUser.getRoles());
        assertFalse(savedUser.isConfirmed());
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(eq("test@example.com"), eq("Registration Confirmation"), anyString());
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

    @Test
    public void testBanUser() {
        user.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.banUser(1L);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertFalse(savedUser.isActive());

        user.setActive(false);

        userService.banUser(1L);

        verify(userRepository, times(2)).save(userCaptor.capture());
        savedUser = userCaptor.getValue();

        assertTrue(savedUser.isActive());
    }

    @Test
    public void testChangeUserRoles() {
        String[] newRoles = {"ROLE_ADMIN", "ROLE_USER"};

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.changeUserRoles(1L, newRoles);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        Set<Role> expectedRoles = new HashSet<>(Arrays.asList(Role.ROLE_ADMIN, Role.ROLE_USER));
        assertEquals(expectedRoles, savedUser.getRoles());
    }

    @Test
    public void testChangeUserRoles_UserNotFound() {
        String[] newRoles = {"ROLE_ADMIN", "ROLE_USER"};

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.changeUserRoles(1L, newRoles);
        });
    }

    @Test
    public void testGetUserByPrincipal() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = userService.getUserByPrincipal(principal);
        assertEquals(user, result);

        result = userService.getUserByPrincipal(null);
        assertNotNull(result);
        assertEquals(new User(), result);
    }

    @Test
    public void testGetUserById() {
        // Test when user is found
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        // Test when user is not found
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        result = userService.getUserById(2L);
        assertFalse(result.isPresent());
    }
}
