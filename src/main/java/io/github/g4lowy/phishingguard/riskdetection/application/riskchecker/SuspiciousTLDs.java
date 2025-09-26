package io.github.g4lowy.phishingguard.riskdetection.application.riskchecker;

import io.github.g4lowy.phishingguard.riskdetection.domain.riskchecker.UrlRiskCheckerStrategy;
import io.github.g4lowy.phishingguard.riskdetection.application.service.UrlUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class SuspiciousTLDs implements UrlRiskCheckerStrategy {

    private final List<String> suspiciousTLDs;

    private SuspiciousTLDs(@Value("${app.riskDetection.urlRiskCheckerStrategy.suspiciousTLDs}") List<String> suspiciousTLDs) {

        this.suspiciousTLDs = suspiciousTLDs;
    }

    @Override
    public boolean breaksRule(String url) {

        String normalized = UrlUtils.normalize(url);
        String host = UrlUtils.hostOf(normalized);

        return suspiciousTLDs.stream().anyMatch(tld -> host != null && host.endsWith(tld));
    }
}
