package com.agan.onlinebuysellplatform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/localization")
public class LocalizationController {

    private final MessageSource messageSource;

    @GetMapping("/messages")
    public Map<String, String> getAllMessages(@RequestParam(name = "lang", defaultValue = "en") String lang) {
        Locale locale = new Locale(lang);
        Map<String, String> messages = new HashMap<>();

        // Load all keys from the localization file
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/messages", locale);

        // Run through all keys and values
        bundle.keySet().forEach(key -> {
            messages.put(key, messageSource.getMessage(key, null, locale));
        });

        return messages;
    }
}

