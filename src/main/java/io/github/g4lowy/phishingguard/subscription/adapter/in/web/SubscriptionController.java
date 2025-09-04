package io.github.g4lowy.phishingguard.subscription.adapter.in.web;


import io.github.g4lowy.phishingguard.ServiceStatus;
import io.github.g4lowy.phishingguard.common.dto.SmsDto;
import io.github.g4lowy.phishingguard.subscription.application.port.in.SubscriptionManagementSmsUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionManagementSmsUseCase subscriptionManagementSmsUseCase;

    @PostMapping
    private Mono<ServiceStatus> handleSubscriptionRequest(@Valid @RequestBody SmsDto smsDto){

        return subscriptionManagementSmsUseCase.handleSubscriptionSms(smsDto);
    }
}
