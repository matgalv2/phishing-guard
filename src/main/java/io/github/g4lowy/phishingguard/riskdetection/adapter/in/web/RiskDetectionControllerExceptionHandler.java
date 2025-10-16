package io.github.g4lowy.phishingguard.riskdetection.adapter.in.web;


import io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.WebRiskClientConnectionException;
import io.github.g4lowy.phishingguard.riskdetection.application.dto.DecisionResponseDto;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Disposition;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Reason;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class RiskDetectionControllerExceptionHandler {

    @ExceptionHandler(WebRiskClientConnectionException.class)
    ResponseEntity<DecisionResponseDto> handleValidationExceptions(WebRiskClientConnectionException webRiskClientConnectionException) {

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new DecisionResponseDto(Disposition.ALLOWED.name(), Reason.SUSPICIOUS_URL.name()));
    }
}
