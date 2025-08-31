package io.github.g4lowy.phishingguard.subscription.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@AllArgsConstructor
@Table("subscriptions")
public class Subscription {

    @Id
    private String msisdn;

    private SubscriptionStatus status;

    @LastModifiedDate
    private Instant updatedAt;

    public Subscription(){}

    public Subscription(String msisdn, SubscriptionStatus status) {

        this.msisdn = msisdn;
        this.status = status;

        updatedAt = Instant.now();
    }

}
