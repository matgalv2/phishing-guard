package io.github.g4lowy.phishingguard.riskdetection.domain.model;

import java.util.List;

public record Decision(Disposition disposition, Reason reason, List<String> checkedUrls) {

    public static Decision allow(Reason reason, List<String> urls){

        return new Decision(Disposition.ALLOWED,  reason, urls);
    }

    public static Decision deny(Reason reason, List<String> urls){

        return new Decision(Disposition.DENIED,  reason, urls);
    }
}
