package io.github.g4lowy.phishingguard.riskdetection.domain.model;

public record Sms(String sender, String recipient, String message) { }