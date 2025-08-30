package io.github.g4lowy.phishingguard.controller;


import io.github.g4lowy.phishingguard.ServiceStatus;
import io.github.g4lowy.phishingguard.dto.SmsDto;
import io.github.g4lowy.phishingguard.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subscription")
public class PhishingGuardSubscriptionController {

    private final SubscriptionService subscriptionService;


    @PostMapping
    private ResponseEntity<ServiceStatus> handleSms(@Valid @RequestBody SmsDto smsDto){

        return null;
    }

}
