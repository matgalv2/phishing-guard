package io.github.g4lowy.phishingguard.riskdetection.application.dto;


public record DecisionResponseDto(
        String disposition,
        String reason
) { }