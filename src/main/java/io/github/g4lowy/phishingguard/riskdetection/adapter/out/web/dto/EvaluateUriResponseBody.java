package io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto;

import java.util.List;

public record EvaluateUriResponseBody(List<Score> scores) {}
