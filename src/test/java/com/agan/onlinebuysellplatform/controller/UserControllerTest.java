package com.agan.onlinebuysellplatform.controller;


import com.agan.onlinebuysellplatform.model.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private Principal principal;
    private Model model;
    private User user;

    @BeforeEach
    void setup() {
        principal = mock(Principal.class);
        model = new ExtendedModelMap();
        user = new User();
    }

    @Test
    @DisplayName("Should return login view with error and logout messages")
    public void testLogin_WithErrorAndLogoutMessages() {
        String error = "error";
        String logout = "logout";

        String viewName = userController.login(error, logout, principal, model);

        assertEquals("login", viewName);
        assertEquals("Invalid email or password", model.getAttribute("error"));
        assertEquals("You have been logged out successfully", model.getAttribute("message"));
    }

    @Test
    @DisplayName("Should return login view when principal is not null")
    public void testLogin_WhenPrincipalIsNotNull() {
        when(userService.getUserByPrincipal(principal)).thenReturn(user);

        String viewName = userController.login(null, null, principal, model);

        assertEquals("login", viewName);
        assertEquals(user, model.getAttribute("user"));
    }

    @Test
    @DisplayName("Should return login view when principal is null")
    public void testLogin_WhenPrincipalIsNull() {
        String viewName = userController.login(null, null, null, model);

        assertEquals("login", viewName);
        assertEquals(null, model.getAttribute("user"));
    }

    @Test
    @DisplayName("Should return profile view with user details")
    public void testProfile_WithUserDetails() {
        when(userService.getUserByPrincipal(principal)).thenReturn(user);

        String viewName = userController.profile(principal, model);

        assertEquals("profile", viewName);
        assertEquals(user, model.getAttribute("user"));
    }

    @Test
    @DisplayName("Should return registration view with user details")
    public void testRegistration_WithUserDetails() {
        when(userService.getUserByPrincipal(principal)).thenReturn(user);

        String viewName = userController.registration(principal, model);

        assertEquals("registration", viewName);
        assertEquals(user, model.getAttribute("user"));
    }

    @Test
    @DisplayName("Should register new user and redirect to login")
    public void testCreateUser_AndRedirectToLogin() throws Exception {
        String viewName = userController.createUser(user, model);

        verify(userService, times(1)).registerNewUser(user);
        assertEquals("redirect:/login", viewName);
    }

    @Test
    @DisplayName("Should handle registration error and return registration view")
    public void testCreateUser_HandleError() throws Exception {
        String errorMessage = "Registration error";
        doThrow(new RuntimeException(errorMessage)).when(userService).registerNewUser(user);

        String viewName = userController.createUser(user, model);

        assertEquals("registration", viewName);
        assertEquals(errorMessage, model.getAttribute("errorMessage"));
    }

    @Test
    @DisplayName("Should confirm user registration and return confirm view")
    public void testConfirmRegistration_AndReturnConfirmView() throws Exception {
        String token = "token";
        when(userService.confirmUser(token)).thenReturn(user);

        String viewName = userController.confirmRegistration(token, model);

        assertEquals("confirm", viewName);
        assertEquals(user, model.getAttribute("user"));
    }

    @Test
    @DisplayName("Should handle confirm registration error and return confirm view")
    public void testConfirmRegistration_HandleError() throws Exception {
        String token = "token";
        String errorMessage = "Confirmation error";
        doThrow(new RuntimeException(errorMessage)).when(userService).confirmUser(token);

        String viewName = userController.confirmRegistration(token, model);

        assertEquals("confirm", viewName);
        assertEquals(errorMessage, model.getAttribute("error"));
    }

    @Test
    @DisplayName("Should return user info view with user details")
    public void testUserInfo_WithUserDetails() {
        when(userService.getUserByPrincipal(principal)).thenReturn(user);

        String viewName = userController.userInfo(user, model, principal);

        assertEquals("user-info", viewName);
        assertEquals(user, model.getAttribute("user"));
        assertEquals(user, model.getAttribute("userByPrincipal"));
        assertEquals(user.getProducts(), model.getAttribute("products"));
    }
}
