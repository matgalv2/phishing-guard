package io.github.g4lowy.phishingguard.subscription.adapter.out.persistance;

import io.github.g4lowy.phishingguard.subscription.adapter.out.persistance.model.SubscriptionEntity;
import io.github.g4lowy.phishingguard.subscription.domain.Subscription;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface R2dbcSubscriptionRepository extends R2dbcRepository<SubscriptionEntity, String> {

    @Query("""
        INSERT INTO subscriptions (msisdn, status, updated_at)
        VALUES (:#{#subscription.msisdn}, :#{#subscription.status}, NOW())
        ON CONFLICT (msisdn)
        DO UPDATE SET status = EXCLUDED.status, updated_at = NOW()
    """)
    Mono<Void> upsert(Subscription subscription);
}
