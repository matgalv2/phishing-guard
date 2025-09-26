package io.github.g4lowy.phishingguard.riskdetection.application.riskchecker;

import io.github.g4lowy.phishingguard.riskdetection.application.service.UrlUtils;
import io.github.g4lowy.phishingguard.riskdetection.domain.riskchecker.UrlRiskCheckerStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuspiciousHostPrefix implements UrlRiskCheckerStrategy {

    private final List<String> hostsPrefixes;

    private SuspiciousHostPrefix(@Value("${app.riskDetection.urlRiskCheckerStrategy.suspiciousHostsPrefixes}")List<String> domainImitations) {

        this.hostsPrefixes = domainImitations;
    }

    @Override
    public boolean breaksRule(String url) {

        String normalizedUrl = UrlUtils.normalize(url);
        String host = UrlUtils.hostOf(normalizedUrl);

        return hostsPrefixes.stream().anyMatch(host::startsWith);
    }
}
