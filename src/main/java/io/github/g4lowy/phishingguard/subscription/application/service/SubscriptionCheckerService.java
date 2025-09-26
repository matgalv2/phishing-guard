package io.github.g4lowy.phishingguard.subscription.application.service;

import io.github.g4lowy.phishingguard.subscription.application.port.in.CheckSubscriptionUseCase;
import io.github.g4lowy.phishingguard.subscription.application.port.out.persistance.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SubscriptionCheckerService implements CheckSubscriptionUseCase {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Mono<Boolean> hasActiveSubscription(String msisdn) {
        return subscriptionRepository.isActive(msisdn);
    }
}
