package io.github.g4lowy.phishingguard.riskdetection.adapter.out.persistance.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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