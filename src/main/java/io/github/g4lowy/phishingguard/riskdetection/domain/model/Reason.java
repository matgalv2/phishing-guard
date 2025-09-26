package io.github.g4lowy.phishingguard.riskdetection.domain.model;

import lombok.Getter;

@Getter
public enum Reason {

    NO_URLS("No urls found in message."),
    SUSPICIOUS_URL("Url(s) look(s) suspicious, but could not be checked via webRisk service."),
    MALICIOUS_URL("Malicious url(s) was/were found in message."),
    NO_MALICIOUS_URL_FOUND("No malicious url(s) was/were found in message."),
    INACTIVE_SUBSCRIPTION("All the traffic is allowed when subscription is inactive");

    private final String message;

    Reason(String message) {
        this.message = message;
    }
}
