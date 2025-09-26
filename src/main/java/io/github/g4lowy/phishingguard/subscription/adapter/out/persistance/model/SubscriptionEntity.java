package io.github.g4lowy.phishingguard.subscription.adapter.out.persistance.model;


import io.github.g4lowy.phishingguard.subscription.domain.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Table("subscriptions")
public class SubscriptionEntity {

    @Id
    private String msisdn;

    private SubscriptionStatus status;

    @LastModifiedDate
    private Instant updatedAt;

}
