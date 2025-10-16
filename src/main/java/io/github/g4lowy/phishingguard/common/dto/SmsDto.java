package io.github.g4lowy.phishingguard.common.dto;

import io.github.g4lowy.phishingguard.common.validation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;

public record SmsDto(@PhoneNumber String sender, @PhoneNumber String recipient, @NotBlank String message) {

}
