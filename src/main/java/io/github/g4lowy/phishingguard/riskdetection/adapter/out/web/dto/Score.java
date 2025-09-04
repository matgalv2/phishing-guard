package io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto;

public record Score(ThreatType threatType, ConfidenceLevel confidenceLevel) {
}
