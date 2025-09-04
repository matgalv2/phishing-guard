package io.github.g4lowy.phishingguard.subscription.application.port.out.persistance;

import io.github.g4lowy.phishingguard.subscription.domain.Subscription;
import io.github.g4lowy.phishingguard.subscription.domain.SubscriptionStatus;
import reactor.core.publisher.Mono;

public interface SubscriptionRepository {

    Mono<Boolean> isActive(String msisdn);
    Mono<SubscriptionStatus> getStatus(String msisdn);
    Mono<Void> upsert(Subscription subscription);

}
