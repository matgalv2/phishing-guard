package io.github.g4lowy.phishingguard.subscription.domain;

import reactor.core.publisher.Mono;

public interface SubscriptionRepository {

    Mono<Boolean> isActive(String msisdn);
    Mono<SubscriptionStatus> getStatus(String msisdn);
    Mono<Void> upsert(Subscription subscription);

}
