package io.github.g4lowy.phishingguard.subscription.infrastructure.repository;


import io.github.g4lowy.phishingguard.subscription.domain.Subscription;
import io.github.g4lowy.phishingguard.subscription.domain.SubscriptionRepository;
import io.github.g4lowy.phishingguard.subscription.domain.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class ReactiveSubscriptionRepository implements SubscriptionRepository {

    private final R2dbcSubscriptionRepository subscriptionRepository;

    public Mono<Boolean> isActive(String msisdn) {
        return getStatus(msisdn).map(status -> status == SubscriptionStatus.ACTIVE);
    }

    public Mono<SubscriptionStatus> getStatus(String msisdn) {
        return subscriptionRepository.findById(msisdn)
                .map(Subscription::getStatus)
                .defaultIfEmpty(SubscriptionStatus.INACTIVE);
    }

    @Override
    public Mono<Void> upsert(Subscription subscription) {
        return subscriptionRepository.findById(subscription.getMsisdn())
                .flatMap(existing -> {

                    existing.setStatus(subscription.getStatus());
                    return subscriptionRepository.save(existing);
                })
                .switchIfEmpty(subscriptionRepository.save(subscription))
                .then();
    }
}
