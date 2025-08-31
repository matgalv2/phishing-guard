package io.github.g4lowy.phishingguard.subscription.domain;

import lombok.Getter;

@Getter
public enum SubscriptionStatus {
    ACTIVE("START"),
    INACTIVE("STOP");

    private final String keyword;

    SubscriptionStatus(String keyword) {
        this.keyword = keyword;
    }
}
