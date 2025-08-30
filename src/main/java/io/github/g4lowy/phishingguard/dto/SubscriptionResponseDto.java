package io.github.g4lowy.phishingguard.dto;

import io.github.g4lowy.phishingguard.domain.SubscriptionStatus;

public record SubscriptionResponseDto(SubscriptionStatus subscriptionStatus, String message) {

}
