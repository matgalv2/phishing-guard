package io.github.g4lowy.phishingguard.riskdetection.application.riskchecker;

import io.github.g4lowy.phishingguard.riskdetection.application.service.UrlUtils;
import io.github.g4lowy.phishingguard.riskdetection.domain.riskchecker.UrlRiskCheckerStrategy;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
class DomainImitation implements UrlRiskCheckerStrategy {

    private final List<String> banksDomains;
    private final int minLevenshteinDistance;
    private final int maxLevenshteinDistance;

    private DomainImitation(@Value("${app.riskDetection.urlRiskCheckerStrategy.banksDomains}") List<String> banksDomains,
                            @Value("${app.riskDetection.urlRiskCheckerStrategy.minLevenshteinDistance}") int minLevenshteinDistance,
                            @Value("${app.riskDetection.urlRiskCheckerStrategy.maxLevenshteinDistance}") int maxLevenshteinDistance) {

        this.banksDomains = banksDomains;
        this.minLevenshteinDistance = minLevenshteinDistance;
        this.maxLevenshteinDistance = maxLevenshteinDistance;
    }

    @Override
    public boolean breaksRule(String url) {

        String normalizedUrl = UrlUtils.normalize(url);
        String host = UrlUtils.hostOf(normalizedUrl);
        String eTldPlusOne = UrlUtils.eTldPlusOne(host);

        return banksDomains.stream()
                .map(bank -> calculateLevenshteinDistance(eTldPlusOne, bank))
                .anyMatch(distance -> distance >= minLevenshteinDistance && distance <= maxLevenshteinDistance);
    }

    private int calculateLevenshteinDistance(String left, String right) {

        return LevenshteinDistance.getDefaultInstance().apply(left, right);
    }
}
