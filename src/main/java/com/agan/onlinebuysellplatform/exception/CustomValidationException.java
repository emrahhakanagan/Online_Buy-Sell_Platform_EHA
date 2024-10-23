package com.agan.onlinebuysellplatform.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomValidationException extends RuntimeException {
    private final Map<String, String> errors;
}