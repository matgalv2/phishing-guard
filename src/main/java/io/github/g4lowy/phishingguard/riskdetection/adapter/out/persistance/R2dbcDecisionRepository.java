package io.github.g4lowy.phishingguard.riskdetection.adapter.out.persistance;


import io.github.g4lowy.phishingguard.riskdetection.adapter.out.persistance.model.DecisionEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

interface R2dbcDecisionRepository extends R2dbcRepository<DecisionEntity, Long> {
}