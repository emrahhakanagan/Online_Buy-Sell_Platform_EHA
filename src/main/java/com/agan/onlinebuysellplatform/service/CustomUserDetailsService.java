package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.isConfirmed()) {
            throw new UsernameNotFoundException("User not found or email not confirmed");
        }
        return user;
    }

}
