package io.github.g4lowy.phishingguard.riskdetection.adapter.in.web;


import io.github.g4lowy.phishingguard.common.dto.SmsDto;
import io.github.g4lowy.phishingguard.riskdetection.application.dto.DecisionResponseDto;
import io.github.g4lowy.phishingguard.riskdetection.application.port.in.EvaluateSmsRiskUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@RestController
@RequestMapping("/sms")
public class RiskDetectionController {

    private final EvaluateSmsRiskUseCase evaluateSmsRiskUseCase;

    @PostMapping
    private Mono<DecisionResponseDto> handleSms(@Valid @RequestBody SmsDto smsDto) {

        return evaluateSmsRiskUseCase.evaluate(smsDto);
    }
}
