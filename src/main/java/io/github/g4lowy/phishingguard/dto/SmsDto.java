package io.github.g4lowy.phishingguard.dto;

import jakarta.validation.constraints.NotBlank;

public record SmsDto(@NotBlank String sender, @NotBlank String recipient, @NotBlank String message) {
}
