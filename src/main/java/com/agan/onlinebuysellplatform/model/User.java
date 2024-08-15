package com.agan.onlinebuysellplatform.model;

import com.agan.onlinebuysellplatform.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(schema = "buyselleha", name = "users")
@Data
@ToString(exclude = {"products"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    @Column(name = "email", unique = true, updatable = false)
    private String email;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must contain only digits and be 10 to 15 characters long")
    @NotBlank(message = "Phone number cannot be blank")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Pattern(regexp = "^[A-Za-z]{2,50}$", message = "Name must contain only letters and be 2 to 50 characters long")
    @NotBlank(message = "Name cannot be blank")
    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image avatar;

    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,20}$", message = "Password must contain at least one digit and one uppercase letter")
    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password", length = 1000)
    private String password;

    @Transient
    @NotBlank(message = "Password confirmation cannot be blank")
    private String passwordConfirmation;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "confirmed")
    private boolean confirmed;

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreated;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Product> products = new ArrayList<>();


    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    // security

    @AssertTrue(message = "Password and password confirmation do not match")
    public boolean isPasswordValid() {
        return password.equals(passwordConfirmation);
    }

    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active && confirmed;
    }
}
