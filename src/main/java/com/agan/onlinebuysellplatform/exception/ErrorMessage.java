package com.agan.onlinebuysellplatform.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorMessage {
    private int statusCode;
    private String message; // Message to the user
    private String technicalDetails; // Details for the logs
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorMessage(int statusCode, String message, String technicalDetails) {
        this.statusCode = statusCode;
        this.message = message;
        this.technicalDetails = technicalDetails;
    }
}

