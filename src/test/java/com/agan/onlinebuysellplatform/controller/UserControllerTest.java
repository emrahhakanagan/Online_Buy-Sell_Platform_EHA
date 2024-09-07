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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.security.Principal;

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
    @DisplayName("Should register new user and redirect to login")
    public void testCreateUser_AndRedirectToLogin() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = userController.createUser(user, bindingResult, model);

        verify(userService, times(1)).registerNewUser(user);
        assertEquals("redirect:/login", viewName);
    }

    @Test
    @DisplayName("Should handle validation errors and return registration view")
    public void testCreateUser_ValidationErrors() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        Model model = mock(Model.class);

        String viewName = userController.createUser(user, bindingResult, model);

        assertEquals("registration", viewName);

        verify(userService, never()).registerNewUser(any(User.class));
    }

    @Test
    @DisplayName("Should handle registration error and return registration view")
    public void testCreateUser_HandleError() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        Model model = mock(Model.class);

        // Симуляция исключения во время регистрации пользователя
        String errorMessage = "Registration error";
        doThrow(new RuntimeException(errorMessage)).when(userService).registerNewUser(any(User.class));

        // Вызов метода контроллера
        String viewName = userController.createUser(user, bindingResult, model);

        // Проверка, что возвращено правильное представление
        assertEquals("registration", viewName);

        // Проверка, что сообщение об ошибке было добавлено в модель
        verify(model).addAttribute("errorMessage", errorMessage);
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
