package com.agan.onlinebuysellplatform.model.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    @DisplayName("Should return correct authority for ROLE_USER")
    public void testGetAuthority_RoleUser() {
        Role role = Role.ROLE_USER;
        assertEquals("ROLE_USER", role.getAuthority());
    }

    @Test
    @DisplayName("Should return correct authority for ROLE_ADMIN")
    public void testGetAuthority_RoleAdmin() {
        Role role = Role.ROLE_ADMIN;
        assertEquals("ROLE_ADMIN", role.getAuthority());
    }
}
