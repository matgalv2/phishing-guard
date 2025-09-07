package io.github.g4lowy.phishingguard.subscription.adapter.out.persistance;


import io.github.g4lowy.phishingguard.subscription.adapter.out.persistance.model.SubscriptionEntity;
import io.github.g4lowy.phishingguard.subscription.domain.Subscription;
import io.github.g4lowy.phishingguard.subscription.application.port.out.persistance.SubscriptionRepository;
import io.github.g4lowy.phishingguard.subscription.domain.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class R2dbcSubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final R2dbcSubscriptionRepository subscriptionRepository;

    @Override
    public Mono<Boolean> isActive(String msisdn) {
        return getStatus(msisdn).map(status -> status == SubscriptionStatus.ACTIVE);
    }

    @Override
    public Mono<SubscriptionStatus> getStatus(String msisdn) {
        return subscriptionRepository.findById(msisdn)
                .map(SubscriptionEntity::getStatus)
                .defaultIfEmpty(SubscriptionStatus.INACTIVE);
    }

    @Override
    public Mono<Void> upsert(Subscription subscription) {
        return subscriptionRepository.upsert(subscription);
    }
}
