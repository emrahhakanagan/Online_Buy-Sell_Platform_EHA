package com.agan.onlinebuysellplatform.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        Locale locale = localeResolver.resolveLocale((HttpServletRequest) request);
        String localizedMessage = messageSource.getMessage("error.user.exists", null, locale);

        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                localizedMessage, // Локализованное сообщение для пользователя
                "User already exists with this email at " + request.getDescription(false)
        );

        log.error("User registration error: {}", errorMessage.getTechnicalDetails());
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    // Пример для валидации
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(CustomValidationException ex, WebRequest request) {
        Locale locale = localeResolver.resolveLocale((HttpServletRequest) request);
        String localizedMessage = messageSource.getMessage("error.validation.failed", null, locale);

        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                localizedMessage,
                ex.getErrors().toString()
        );

        log.error("Validation error: {}", ex.getErrors());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}

