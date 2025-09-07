package io.github.g4lowy.phishingguard.riskdetection.adapter.out.persistance.model;


import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("decisions")
public class DecisionEntity {

    @Id
    private Long id;

    private String sender;
    private String recipient;
    private String disposition;
    private String reason;
    private String[] urls;
}