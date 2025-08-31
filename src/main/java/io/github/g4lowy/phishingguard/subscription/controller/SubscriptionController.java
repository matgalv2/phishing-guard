package io.github.g4lowy.phishingguard.subscription.controller;


import io.github.g4lowy.phishingguard.ServiceStatus;
import io.github.g4lowy.phishingguard.dto.SmsDto;
import io.github.g4lowy.phishingguard.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    private ResponseEntity<Mono<ServiceStatus>> handleSms(@Valid @RequestBody SmsDto smsDto){

        return ResponseEntity.ok(subscriptionService.handleSms(smsDto));
    }

}
