package io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto;

import java.util.List;

public record EvaluateUriRequestBody(String uri, List<ThreatType> threatTypes, boolean allowScan) {

}
