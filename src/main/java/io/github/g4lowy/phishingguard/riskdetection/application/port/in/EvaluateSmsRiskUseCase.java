package io.github.g4lowy.phishingguard.riskdetection.application.port.in;

import io.github.g4lowy.phishingguard.common.dto.SmsDto;
import io.github.g4lowy.phishingguard.riskdetection.application.dto.DecisionResponseDto;
import reactor.core.publisher.Mono;

public interface EvaluateSmsRiskUseCase {

    Mono<DecisionResponseDto> evaluate(SmsDto smsDto);
}
