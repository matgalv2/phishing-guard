package io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto;

import java.util.Arrays;
import java.util.List;

public enum ThreatType {

    THREAT_TYPE_UNSPECIFIED,
    SOCIAL_ENGINEERING,
    MALWARE,
    UNWANTED_SOFTWARE;

    public static List<ThreatType> valuesAsList() {

        return Arrays.stream(values()).toList();
    }
}
