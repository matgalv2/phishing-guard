package io.github.g4lowy.phishingguard.riskdetection.domain.riskchecker;

public interface UrlRiskCheckerStrategy {

    boolean breaksRule(String url);
}
