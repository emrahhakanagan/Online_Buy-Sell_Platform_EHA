package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.exception.UserNotFoundException;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.model.enums.Role;
import com.agan.onlinebuysellplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    void setUpUser() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setConfirmed(false);
    }

    @Test
    @DisplayName("Should save user with encoded password and send email when valid user is provided")
    public void registerNewUser_WhenValidUserIsProvided() throws Exception {
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
        verify(emailService, times(1))
                .sendEmail(eq("test@example.com"), eq("Registration Confirmation"), anyString());
    }

    @Test
    @DisplayName("Should throw exception when registering user fails")
    public void testRegisterNewUser_WhenRegistrationFails() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerNewUser(user);
        });

        String expectedMessage = "Database error";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(0))
                .sendEmail(eq("test@example.com"), eq("Registration Confirmation"), anyString());
    }

    @Test
    @DisplayName("Should confirm user when valid token is provided")
    public void testConfirmUser_WhenValidTokenIsProvided() throws Exception {
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
    @DisplayName("Should throw exception when invalid token is provided")
    public void testConfirmUser_WhenInvalidTokenIsProvided() {
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
    @DisplayName("Should ban user when user is active")
    public void testBanUser_WhenUserIsActive() {
        user.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.banUser(1L);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertFalse(savedUser.isActive());
    }

    @Test
    @DisplayName("Should unban user when user is inactive")
    public void testUnbanUser_WhenUserIsInactive() {
        user.setActive(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.banUser(1L);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertTrue(savedUser.isActive());
    }

    @Test
    @DisplayName("Should update user roles when user is found")
    public void testChangeUserRoles_WhenUserIsFound() {
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
    @DisplayName("Should throw exception when user is not found for changing roles")
    public void testChangeUserRoles_WhenUserIsNotFound() {
        String[] newRoles = {"ROLE_ADMIN", "ROLE_USER"};

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.changeUserRoles(1L, newRoles);
        });

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertFalse(actualMessage.contains(expectedMessage));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("Should return user when principal is not null and user exists")
    public void testGetUserByPrincipal_WhenPrincipalIsNotNullAndUserExists() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = userService.getUserByPrincipal(principal);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Should return new user when principal is null")
    public void testGetUserByPrincipal_WhenPrincipalIsNull() {
        User result = userService.getUserByPrincipal(null);
        assertNotNull(result);
        assertEquals(new User(), result);
    }

    @Test
    @DisplayName("Should return null when principal is not null and user does not exist")
    public void testGetUserByPrincipal_WhenPrincipalIsNotNullAndUserDoesNotExist() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        User result = userService.getUserByPrincipal(principal);
        assertNull(result);
    }

    @Test
    @DisplayName("Should return user when user is found by ID")
    public void testGetUserById_WhenUserIsFound() {
        // Настраиваем репозиторий для возврата пользователя, когда он найден
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("Should return empty optional when user is not found by ID")
    public void testGetUserById_WhenUserIsNotFound() {
        // Настраиваем репозиторий для возврата пустого значения, когда пользователь не найден
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return user when email exists")
    void testFindUserByEmail_WhenEmailExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        User result = userService.findUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should throw RuntimeException when email is null")
    void testFindUserByEmail_WhenEmailIsNull() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findUserByEmail(null);
        });

        assertEquals("Email: null does not exist", exception.getMessage());
    }
}
