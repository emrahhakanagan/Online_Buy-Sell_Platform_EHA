package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.config.TestConfig;
import com.agan.onlinebuysellplatform.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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

public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("Should find user by email")
    public void testFindByEmail_WhenUserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setConfirmed(true);
        user.setActive(true);

        userRepository.save(user);

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
    @Transactional
    public void testFindByConfirmationToken_WhenTokenExists() {
        String token = "existingToken";
        User user = new User();
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