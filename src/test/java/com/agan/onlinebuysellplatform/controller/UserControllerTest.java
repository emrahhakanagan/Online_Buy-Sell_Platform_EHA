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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
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
    private BindingResult bindingResult;

    @BeforeEach
    void setup() {
        principal = mock(Principal.class);
        model = new ExtendedModelMap();
        user = new User();
        bindingResult = mock(BindingResult.class);
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
        assertNull(model.getAttribute("user"));
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
    @DisplayName("Should return registration view with a new user")
    public void testRegistration_WithNewUser() {
        String viewName = userController.registration(model);

        assertEquals("registration", viewName);

        assertInstanceOf(User.class, model.getAttribute("user"));
    }

    @Test
    @DisplayName("Should register new user and return HTTP 200")
    public void testCreateUser_SuccessfulRegistration() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<?> response = userController.createUser(user, bindingResult);

        verify(userService, times(1)).registerNewUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle validation errors and return HTTP 400 with errors")
    public void testCreateUser_ValidationErrors() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(true);

        FieldError fieldError = new FieldError("user", "email", "Invalid email format");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<?> response = userController.createUser(user, bindingResult);

        verify(userService, never()).registerNewUser(any(User.class));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, Object> outerErrors = (Map<String, Object>) response.getBody();
        assertNotNull(outerErrors);

        Map<String, String> errors = (Map<String, String>) outerErrors.get("errors");
        assertNotNull(errors, "Errors map should not be null");

        assertTrue(errors.containsKey("email"), "Errors should contain 'email' field");

        assertEquals("Invalid email format", errors.get("email"));
    }

    @Test
    @DisplayName("Should handle registration error and return HTTP 500 with error message")
    public void testCreateUser_HandleError() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);

        String errorMessage = "Registration error";
        doThrow(new RuntimeException(errorMessage)).when(userService).registerNewUser(any(User.class));

        ResponseEntity<?> response = userController.createUser(user, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, String> errorBody = (Map<String, String>) response.getBody();
        assertNotNull(errorBody);
        assertEquals(errorMessage, errorBody.get("error"));
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
