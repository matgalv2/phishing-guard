package io.github.g4lowy.phishingguard.subscription.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum SubscriptionStatus {
    ACTIVE("START"),
    INACTIVE("STOP");

    //TODO: key words should not be hardcoded

    private final String keyword;

    SubscriptionStatus(String keyword) {
        this.keyword = keyword;
    }

    public static Optional<SubscriptionStatus> findStatusByKeyword(String message) {
        return Arrays.stream(SubscriptionStatus.values())
                .filter(status -> status.getKeyword().equals(message))
                .findFirst();
    }
}
