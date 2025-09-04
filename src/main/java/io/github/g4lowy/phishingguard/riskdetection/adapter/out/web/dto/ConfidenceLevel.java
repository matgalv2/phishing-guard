package io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto;

public enum ConfidenceLevel {

    CONFIDENCE_LEVEL_UNSPECIFIED(0),
    SAFE(1),
    LOW(2),
    MEDIUM(2),
    HIGH(4),
    HIGHER(5),
    VERY_HIGH(6),
    EXTREMELY_HIGH(7);

    private final int rank;

    ConfidenceLevel(int rank) {
        this.rank = rank;
    }

    public static boolean isMoreDangerousThan(ConfidenceLevel level, ConfidenceLevel threshold) {

        return level.rank > threshold.rank;
    }
}
