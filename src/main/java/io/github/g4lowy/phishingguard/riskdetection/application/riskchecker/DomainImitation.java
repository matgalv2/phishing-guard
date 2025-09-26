package io.github.g4lowy.phishingguard.riskdetection.application.riskchecker;

import io.github.g4lowy.phishingguard.riskdetection.application.service.UrlUtils;
import io.github.g4lowy.phishingguard.riskdetection.domain.riskchecker.UrlRiskCheckerStrategy;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
class DomainImitation implements UrlRiskCheckerStrategy {


    private final List<String> domainImitations;

    private DomainImitation(@Value("${app.riskDetection.urlRiskCheckerStrategy.banksDomains}") List<String> domainImitations) {

        this.domainImitations = domainImitations;
    }

    @Override
    public boolean breaksRule(String url) {

        String normalizedUrl = UrlUtils.normalize(url);
        String host = UrlUtils.hostOf(normalizedUrl);
        String eTldPlusOne = UrlUtils.eTldPlusOne(host);

        return domainImitations.stream()
                .map(bank -> calculateLevenshteinDistance(eTldPlusOne, bank))
                .anyMatch(distance -> distance >= 1 && distance <= 2);
    }

    private int calculateLevenshteinDistance(String left, String right) {

        return LevenshteinDistance.getDefaultInstance().apply(left, right);
    }
}
