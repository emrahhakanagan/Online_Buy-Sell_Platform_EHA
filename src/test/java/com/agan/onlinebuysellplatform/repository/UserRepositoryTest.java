package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

// ----- first version----- //
//@ActiveProfiles("test")
//@Import(TestConfig.class)
//@DataJpaTest

// ----- second version----- //
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPhoneNumber("1234567890");
        user.setPassword("Password1");
        user.setPasswordConfirmation("Password1");
        user.setConfirmed(true);
        user.setActive(true);

        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should save user successfully")
    public void testSaveUser() {
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("Test User", savedUser.getName());
    }

    @Test
    @DisplayName("Should find user by email")
    public void testFindByEmail_WhenUserExists() {
        User foundUser = userRepository.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    @DisplayName("Should return null when user with email does not exist")
    public void testFindByEmail_WhenUserDoesNotExist() {
        String email = "nonexistent@example.com";

        User foundUser = userRepository.findByEmail(email);

        assertNull(foundUser, "User with email " + email + " should not be found");
    }

    @Test
    @DisplayName("Should return user when confirmation token exists")
    public void testFindByConfirmationToken_WhenTokenExists() {
        String token = "existingToken";

        user.setConfirmationToken(token);

        entityManager.persist(user);
        entityManager.flush();

        User foundUser = userRepository.findByConfirmationToken(token);

        assertNotNull(foundUser, "User with token " + token + " should be found");
        assertEquals(token, foundUser.getConfirmationToken(), "Returned user should have the correct confirmation token");
    }

    @Test
    @DisplayName("Should return null when confirmation token does not exist")
    public void testFindByConfirmationToken_WhenTokenDoesNotExist() {
        String token = "nonexistentToken";

        User foundUser = userRepository.findByConfirmationToken(token);

        assertNull(foundUser, "User with token " + token + " should not be found");
    }

}