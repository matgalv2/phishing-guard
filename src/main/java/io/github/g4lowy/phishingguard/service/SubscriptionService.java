package io.github.g4lowy.phishingguard.service;

import io.github.g4lowy.phishingguard.domain.SubscriptionStatus;
import io.github.g4lowy.phishingguard.dto.SmsDto;
import io.github.g4lowy.phishingguard.dto.SubscriptionResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final

    public Mono<SubscriptionResponseDto> handleSMS(SmsDto smsDto){



    }

    private static Optional<SubscriptionStatus> findStatusByName(String name) {
        return Arrays.stream(SubscriptionStatus.values())
                .filter(status -> status.name().equals(name))
                .findFirst();
    }

}
