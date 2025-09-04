package io.github.g4lowy.phishingguard.riskdetection.application.service;

import io.github.g4lowy.phishingguard.common.dto.SmsDto;
import io.github.g4lowy.phishingguard.riskdetection.application.dto.DecisionResponseDto;
import io.github.g4lowy.phishingguard.riskdetection.application.port.in.EvaluateSmsRiskUseCase;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Decision;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Disposition;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Reason;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Sms;
import io.github.g4lowy.phishingguard.riskdetection.application.port.out.DecisionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiskDetectionService implements EvaluateSmsRiskUseCase {

    private final UrlEvaluateService urlEvaluateService;
    private final DecisionRepositoryPort decisionRepositoryPort;

    public Mono<DecisionResponseDto> evaluate(SmsDto smsDto) {

        Sms sms = new Sms(smsDto.sender(), smsDto.recipient(), smsDto.message());

        List<String> urls = UrlUtils.extractUrls(sms.message());

        if (urls.isEmpty()){

            return Mono.just(new DecisionResponseDto(Disposition.ALLOWED.name(), Reason.NO_URLS.name()));
        }

        return urlEvaluateService.evaluateUrls(urls)
                .map(risk ->
                    switch(risk) {
                        case SAFE -> Decision.allow(Reason.NO_MALICIOUS_URL_FOUND, urls);
                        case MALICIOUS -> Decision.deny(Reason.MALICIOUS_URL, urls);
                    }
                )
                .flatMap(decision -> decisionRepositoryPort.saveDecision(sms, decision).thenReturn(decision))
                .map(decision -> new DecisionResponseDto(decision.disposition().name(), decision.reason().name()));
    }
}