package com.agan.onlinebuysellplatform.controller;


import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.model.enums.Role;
import com.agan.onlinebuysellplatform.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    private Long userId;

    private Model model;

    @BeforeEach
    void setup() {
        userId = 1L;
        model = new ExtendedModelMap();
    }

    @Test
    @DisplayName("Should return admin view with users and admin info")
    public void testAdminView_WithUsersAndAdminDetails() {
        List<User> users = Arrays.asList(new User(), new User());
        User admin = new User();
        when(userService.list()).thenReturn(users);
        when(userService.getUserByPrincipal(any(Principal.class))).thenReturn(admin);

        String viewName = adminController.admin(model, mock(Principal.class));

        assertEquals("admin", viewName);
        assertEquals(users, model.getAttribute("users"));
        assertEquals(admin, model.getAttribute("user"));
    }

    @Test
    @DisplayName("Should return admin view when no users")
    public void testAdminView_WhenNoUsersExist() {
        when(userService.list()).thenReturn(Collections.emptyList());
        when(userService.getUserByPrincipal(any(Principal.class))).thenReturn(new User());

        String viewName = adminController.admin(model, mock(Principal.class));

        assertEquals("admin", viewName);
        assertTrue(((List<User>) model.getAttribute("users")).isEmpty());
    }

    @Test
    @DisplayName("Should ban user and redirect to admin")
    public void testUserBan_AndRedirectToAdmin() {

        String viewName = adminController.userBan(userId);

        verify(userService, times(1)).banUser(userId);
        assertEquals("redirect:/admin", viewName);
    }

    @Test
    @DisplayName("Should return user-edit view with user details when user exists")
    public void testUserEdit_Get() {
        Long userId = 1L;
        String username = "admin";
        User user = new User();
        user.setId(userId);
        user.setEmail(username); // Assuming email is used as the principal name
        user.setRoles(new HashSet<>(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN)));

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(userService.getUserByPrincipal(any(Principal.class))).thenReturn(user);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        Model model = mock(Model.class);

        String viewName = adminController.userEdit(userId, model, principal);

        assertEquals("user-edit", viewName);
        verify(model).addAttribute("editUser", user);
        verify(model).addAttribute("admin", user);
        verify(model).addAttribute(eq("roles"), any());
    }

    @Test
    @DisplayName("Should redirect to admin when user not found")
    public void testUserEdit_Get_UserNotFound() {
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        String viewName = adminController.userEdit(userId, model, mock(Principal.class));

        assertEquals("redirect:/admin", viewName);
    }

    @Test
    @DisplayName("Should change user roles and redirect to admin")
    public void testChangeUserRoles_AndRedirectToAdmin() {
        String[] roles = {"ROLE_USER", "ROLE_ADMIN"};

        String viewName = adminController.userEdit(roles, userId);

        verify(userService, times(1)).changeUserRoles(userId, roles);
        assertEquals("redirect:/admin", viewName);
    }

    @Test
    @DisplayName("Should handle invalid roles gracefully")
    public void testHandleInvalidRoles_Gracefully() {
        String[] roles = {};

        doThrow(new RuntimeException("Invalid roles")).when(userService).changeUserRoles(userId, roles);

        assertThrows(RuntimeException.class, () -> {
            adminController.userEdit(roles, userId);
        });
    }
}