package io.github.g4lowy.phishingguard.subscription.application.service;

import io.github.g4lowy.phishingguard.subscription.application.dto.ServiceStatusDto;
import io.github.g4lowy.phishingguard.common.dto.SmsDto;
import io.github.g4lowy.phishingguard.subscription.application.port.in.SubscriptionManagementSmsUseCase;
import io.github.g4lowy.phishingguard.subscription.application.port.out.persistance.SubscriptionRepository;
import io.github.g4lowy.phishingguard.subscription.domain.Subscription;
import io.github.g4lowy.phishingguard.subscription.domain.SubscriptionStatus;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class SubscriptionService implements SubscriptionManagementSmsUseCase {

    private static final String ACTIVATION_MESSAGE = "subscription.activated";
    private static final String DEACTIVATION_MESSAGE = "subscription.deactivated";
    private static final String WRONG_CODE_MESSAGE = "subscription.wrongCode";

    private final SubscriptionRepository subscriptionRepository;
    private final MessageSource messageSource;


    private SubscriptionService(SubscriptionRepository subscriptionRepository, MessageSource messageSource) {

        this.subscriptionRepository = subscriptionRepository;
        this.messageSource = messageSource;
    }

    @Override
    public Mono<ServiceStatusDto> handleSubscriptionSms(SmsDto smsDto) {
        return SubscriptionStatus.findStatusByKeyword(smsDto.message())
                .map(subscriptionStatus ->
                        subscriptionRepository
                                .upsert(new Subscription(smsDto.sender(), subscriptionStatus))
                                .then(Mono.fromCallable(() -> subscriptionStatus == SubscriptionStatus.ACTIVE ? ACTIVATION_MESSAGE : DEACTIVATION_MESSAGE))
                )
                .orElse(Mono.just(WRONG_CODE_MESSAGE))
                .map(this::getMessage)
                .map(ServiceStatusDto::new);
    }

    private String getMessage(String key) {

        return messageSource.getMessage(key, null, null);
    }
}
