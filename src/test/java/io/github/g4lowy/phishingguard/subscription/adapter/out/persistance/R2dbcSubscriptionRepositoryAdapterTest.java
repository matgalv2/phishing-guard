package io.github.g4lowy.phishingguard.subscription.adapter.out.persistance;

import io.github.g4lowy.phishingguard.common.IntegrationTestBase;
import io.github.g4lowy.phishingguard.subscription.application.port.out.persistance.SubscriptionRepository;
import io.github.g4lowy.phishingguard.subscription.domain.Subscription;
import io.github.g4lowy.phishingguard.subscription.domain.SubscriptionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;


@DataR2dbcTest
@Testcontainers
@Import(R2dbcSubscriptionRepositoryAdapter.class)
class R2dbcSubscriptionRepositoryAdapterTest extends IntegrationTestBase {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setup() {
        databaseClient.sql("DELETE FROM subscriptions").fetch().rowsUpdated().block();
    }

    @Test
    void shouldReturnInactiveWhenSubscriptionDoesNotExist() {
        StepVerifier.create(subscriptionRepository.getStatus("12345"))
                .expectNext(SubscriptionStatus.INACTIVE)
                .verifyComplete();
    }

    @Test
    void shouldUpsertAndFindActiveSubscription() {
        Subscription subscription = new Subscription("12345", SubscriptionStatus.ACTIVE);

        StepVerifier.create(subscriptionRepository.upsert(subscription))
                .verifyComplete();

        StepVerifier.create(subscriptionRepository.getStatus("12345"))
                .expectNext(SubscriptionStatus.ACTIVE)
                .verifyComplete();

        StepVerifier.create(subscriptionRepository.isActive("12345"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldUpdateStatusWhenUpsertingSameMsisdn() {
        Subscription active = new Subscription("12345", SubscriptionStatus.ACTIVE);
        Subscription inactive = new Subscription("12345", SubscriptionStatus.INACTIVE);

        subscriptionRepository.upsert(active).block();
        subscriptionRepository.upsert(inactive).block();

        StepVerifier.create(subscriptionRepository.getStatus("12345"))
                .expectNext(SubscriptionStatus.INACTIVE)
                .verifyComplete();
    }
}
