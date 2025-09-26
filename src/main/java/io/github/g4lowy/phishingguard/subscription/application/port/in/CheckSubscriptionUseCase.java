package io.github.g4lowy.phishingguard.subscription.application.port.in;

import reactor.core.publisher.Mono;

public interface CheckSubscriptionUseCase {

    Mono<Boolean> hasActiveSubscription(String msisdn);
}
