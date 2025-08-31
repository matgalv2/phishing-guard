package io.github.g4lowy.phishingguard.subscription.service;

import io.github.g4lowy.phishingguard.ServiceStatus;
import io.github.g4lowy.phishingguard.dto.SmsDto;
import io.github.g4lowy.phishingguard.subscription.domain.Subscription;
import io.github.g4lowy.phishingguard.subscription.domain.SubscriptionRepository;
import io.github.g4lowy.phishingguard.subscription.domain.SubscriptionStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final String activationMessage;
    private final String deactivationMessage;
    private final String wrongCodeMessage;

    private final SubscriptionRepository subscriptionRepository;


    private SubscriptionService(SubscriptionRepository subscriptionRepository,
                                @Value("${subscription.service.activated}") String activationMessage,
                                @Value("${subscription.service.deactivated}") String deactivationMessage,
                                @Value("${subscription.service.wrongCode}") String wrongCodeMessage) {

        this.subscriptionRepository = subscriptionRepository;
        this.activationMessage = activationMessage;
        this.deactivationMessage = deactivationMessage;
        this.wrongCodeMessage = wrongCodeMessage;
    }


    public Mono<Boolean> isActive(String msisdn) {

        return subscriptionRepository.isActive(msisdn);
    }

    public Mono<ServiceStatus> handleSms(SmsDto smsDto) {
        return findStatusByKeyword(smsDto.message())
                .map(subscriptionStatus ->
                        subscriptionRepository.upsert(new Subscription(smsDto.sender(), subscriptionStatus))
                                .then(Mono.fromCallable(() -> subscriptionStatus == SubscriptionStatus.ACTIVE ? activationMessage : deactivationMessage))
                )
                .orElse(Mono.just(wrongCodeMessage))
                .map(ServiceStatus::new);
    }

    private static Optional<SubscriptionStatus> findStatusByKeyword(String message) {
        return Arrays.stream(SubscriptionStatus.values())
                .filter(status -> status.getKeyword().equals(message))
                .findFirst();
    }
}
