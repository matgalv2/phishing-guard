package io.github.g4lowy.phishingguard.common.dto;

import java.io.Serializable;
import java.util.List;

public record ErrorMessageDto(List<String> messages) {
}
