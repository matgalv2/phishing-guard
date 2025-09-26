package io.github.g4lowy.phishingguard.subscription.domain;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Subscription {

    private String msisdn;

    private SubscriptionStatus status;
}
