package com.agan.onlinebuysellplatform.validation;

import com.agan.onlinebuysellplatform.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, User> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        return user.getPassword() != null && user.getPassword().equals(user.getPasswordConfirmation());
    }
}
