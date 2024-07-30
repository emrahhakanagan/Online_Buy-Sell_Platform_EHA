package com.agan.onlinebuysellplatform.repository;

import com.agan.onlinebuysellplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByConfirmationToken(String confirmationToken);
}
