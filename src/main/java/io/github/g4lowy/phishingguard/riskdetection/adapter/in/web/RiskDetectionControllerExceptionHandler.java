package io.github.g4lowy.phishingguard.riskdetection.adapter.in.web;


import io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.WebRiskClientConnectionException;
import io.github.g4lowy.phishingguard.riskdetection.application.dto.DecisionResponseDto;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Disposition;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Reason;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RiskDetectionControllerExceptionHandler {

    @ExceptionHandler(WebRiskClientConnectionException.class)
    ResponseEntity<DecisionResponseDto> handleValidationExceptions(WebRiskClientConnectionException webRiskClientConnectionException) {

        // TODO: do przemyślenia, jaki kod najlepiej zwrócić
        //  połączenie z serwerem się nie powiodło, ale sprawdziliśmy heurystykami urle i były podejrzane
        return ResponseEntity.internalServerError().body(new DecisionResponseDto(Disposition.BLOCKED.name(), Reason.SUSPICIOUS_URL.name()));
    }

}
