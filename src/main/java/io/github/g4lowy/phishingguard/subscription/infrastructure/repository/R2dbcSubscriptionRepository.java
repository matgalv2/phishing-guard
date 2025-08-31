package io.github.g4lowy.phishingguard.subscription.infrastructure.repository;

import io.github.g4lowy.phishingguard.subscription.domain.Subscription;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface R2dbcSubscriptionRepository extends R2dbcRepository<Subscription, String> {
}
