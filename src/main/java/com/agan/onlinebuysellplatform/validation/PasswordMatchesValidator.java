package com.agan.onlinebuysellplatform.validation;

import com.agan.onlinebuysellplatform.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, User> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        log.info("Password: {}", user.getPassword());
        log.info("Password Confirmation: {}", user.getPasswordConfirmation());
        return user.getPassword() != null && user.getPassword().equals(user.getPasswordConfirmation());
    }
}
