package io.github.g4lowy.phishingguard.subscription.application.port.in;

import io.github.g4lowy.phishingguard.ServiceStatus;
import io.github.g4lowy.phishingguard.common.dto.SmsDto;
import reactor.core.publisher.Mono;

public interface SubscriptionManagementSmsUseCase {

    Mono<ServiceStatus> handleSubscriptionSms(SmsDto smsDto);
}
