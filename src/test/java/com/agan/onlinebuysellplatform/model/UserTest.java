package com.agan.onlinebuysellplatform.model;

import com.agan.onlinebuysellplatform.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserTest {

    private User user;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Should create user with default values")
    public void testCreateUser_WithDefaultValues() throws Exception {
        Method initMethod = User.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);
        initMethod.invoke(user);

        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getPhoneNumber());
        assertNull(user.getName());
        assertFalse(user.isActive());
        assertNull(user.getAvatar());
        assertNull(user.getPassword());
        assertNull(user.getConfirmationToken());
        assertFalse(user.isConfirmed());
        assertNotNull(user.getDateOfCreated());
        assertTrue(user.getRoles().isEmpty());
        assertTrue(user.getProducts().isEmpty());
    }

    @Test
    @DisplayName("Should initialize date of created")
    public void testInit() throws Exception {
        Method initMethod = User.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);
        initMethod.invoke(user);

        assertNotNull(user.getDateOfCreated());
    }

    @Test
    @DisplayName("Should set and get email")
    public void testSetGetEmail() {
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    @DisplayName("Should set and get phone number")
    public void testSetGetPhoneNumber() {
        String phoneNumber = "123456789";
        user.setPhoneNumber(phoneNumber);
        assertEquals(phoneNumber, user.getPhoneNumber());
    }

    @Test
    @DisplayName("Should set and get name")
    public void testSetGetName() {
        String name = "Test User";
        user.setName(name);
        assertEquals(name, user.getName());
    }

    @Test
    @DisplayName("Should set and get active status")
    public void testSetGetActive() {
        user.setActive(true);
        assertTrue(user.isActive());
    }

    @Test
    @DisplayName("Should set and get avatar")
    public void testSetGetAvatar() {
        Image avatar = new Image();
        user.setAvatar(avatar);
        assertEquals(avatar, user.getAvatar());
    }

    @Test
    @DisplayName("Should set and get password")
    public void testSetGetPassword() {
        String password = "password";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    @DisplayName("Should set and get confirmation token")
    public void testSetGetConfirmationToken() {
        String token = "token";
        user.setConfirmationToken(token);
        assertEquals(token, user.getConfirmationToken());
    }

    @Test
    @DisplayName("Should set and get confirmed status")
    public void testSetGetConfirmed() {
        user.setConfirmed(true);
        assertTrue(user.isConfirmed());
    }

    @Test
    @DisplayName("Should set and get date of created")
    public void testSetGetDateOfCreated() {
        LocalDateTime dateTime = LocalDateTime.now();
        user.setDateOfCreated(dateTime);
        assertEquals(dateTime, user.getDateOfCreated());
    }

    @Test
    @DisplayName("Should set and get roles")
    public void testSetGetRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
    }

    @Test
    @DisplayName("Should set and get products")
    public void testSetGetProducts() {
        Product product = new Product();
        user.setProducts(Collections.singletonList(product));
        assertEquals(1, user.getProducts().size());
        assertEquals(product, user.getProducts().get(0));
    }

    @Test
    @DisplayName("Should check if user is admin")
    public void testIsAdmin() {
        user.setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN)));
        assertTrue(user.isAdmin());
    }

    @Test
    @DisplayName("Should get authorities")
    public void testGetAuthorities() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        assertEquals(roles, user.getAuthorities());
    }

    @Test
    @DisplayName("Should get username")
    public void testGetUsername() {
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getUsername());
    }

    @Test
    @DisplayName("Should check if account is non-expired")
    public void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    @DisplayName("Should check if account is non-locked")
    public void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should check if credentials are non-expired")
    public void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Should check if user is enabled")
    public void testIsEnabled() {
        user.setActive(true);
        user.setConfirmed(true);
        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("Should return empty authorities when user has no roles")
    public void testGetAuthorities_WhenNoRoles() {
        user.setRoles(Collections.emptySet());
        assertTrue(user.getAuthorities().isEmpty(), "Authorities should be empty when user has no roles");
    }

    @Test
    @DisplayName("Should not be enabled when user is inactive")
    public void testIsEnabled_WhenInactive() {
        user.setActive(false);
        user.setConfirmed(true);
        assertFalse(user.isEnabled(), "User should not be enabled when inactive");
    }

    @Test
    @DisplayName("Should not be enabled when user is not confirmed")
    public void testIsEnabled_WhenNotConfirmed() {
        user.setActive(true);
        user.setConfirmed(false);
        assertFalse(user.isEnabled(), "User should not be enabled when not confirmed");
    }

    @Test
    @DisplayName("Should automatically initialize date of creation on pre persist")
    public void testInit_AutoInitialization() {
        user = new User();
        assertNull(user.getDateOfCreated(), "Date of creation should be null before init");

        entityManager.persist(user);
        assertNotNull(user.getDateOfCreated(), "Date of creation should be initialized");
    }
}