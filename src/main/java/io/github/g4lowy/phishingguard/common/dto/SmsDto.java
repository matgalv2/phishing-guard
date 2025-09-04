package io.github.g4lowy.phishingguard.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SmsDto(@Pattern(regexp = PHONE_NUMBER_PATTERN, message = PHONE_NUMBER_VALIDATION_MESSAGE) String sender,
                     @Pattern(regexp = PHONE_NUMBER_PATTERN, message = PHONE_NUMBER_VALIDATION_MESSAGE) String recipient,
                     @NotBlank(message = MESSAGE_VALIDATION_MESSAGE) String message) {

    private static final String PHONE_NUMBER_PATTERN = "\\d{9}";
    private static final String PHONE_NUMBER_VALIDATION_MESSAGE = "Phone number should consists of 9 numbers";
    private static final String MESSAGE_VALIDATION_MESSAGE = "Message should not be blank";
}
