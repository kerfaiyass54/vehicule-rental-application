package com.projecttuto.vehicule_rental.events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record BudgetEvent(
        @JsonProperty("event_id") String eventId,
        @JsonProperty("event_type") String eventType,
        @JsonProperty("entity_type") String entityType,
        @JsonProperty("entity_id") String entityId,
        String email,
        Double budget,
        @JsonProperty("previous_budget") Double previousBudget,
        Instant date
) {
}
