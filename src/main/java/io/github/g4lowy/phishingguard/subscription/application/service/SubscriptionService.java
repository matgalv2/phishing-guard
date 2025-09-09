package io.github.g4lowy.phishingguard.subscription.application.service;

import io.github.g4lowy.phishingguard.subscription.application.dto.ServiceStatusDto;
import io.github.g4lowy.phishingguard.common.dto.SmsDto;
import io.github.g4lowy.phishingguard.subscription.application.port.in.SubscriptionManagementSmsUseCase;
import io.github.g4lowy.phishingguard.subscription.application.port.out.persistance.SubscriptionRepository;
import io.github.g4lowy.phishingguard.subscription.domain.Subscription;
import io.github.g4lowy.phishingguard.subscription.domain.SubscriptionStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SubscriptionService implements SubscriptionManagementSmsUseCase {

    private final String activationMessage;
    private final String deactivationMessage;
    private final String wrongCodeMessage;

    private final SubscriptionRepository subscriptionRepository;


    private SubscriptionService(SubscriptionRepository subscriptionRepository,
                                @Value("${app.message.subscription.activated}") String activationMessage,
                                @Value("${app.message.subscription.deactivated}") String deactivationMessage,
                                @Value("${app.message.subscription.wrongCode}") String wrongCodeMessage) {

        this.subscriptionRepository = subscriptionRepository;
        this.activationMessage = activationMessage;
        this.deactivationMessage = deactivationMessage;
        this.wrongCodeMessage = wrongCodeMessage;
    }

    public Mono<ServiceStatusDto> handleSubscriptionSms(SmsDto smsDto) {
        return SubscriptionStatus.findStatusByKeyword(smsDto.message())
                .map(subscriptionStatus ->
                        subscriptionRepository
                                .upsert(new Subscription(smsDto.sender(), subscriptionStatus))
                                .then(Mono.fromCallable(() -> subscriptionStatus == SubscriptionStatus.ACTIVE ? activationMessage : deactivationMessage))
                )
                .orElse(Mono.just(wrongCodeMessage))
                .map(ServiceStatusDto::new);
    }
}
