package com.projecttuto.vehicule_rental.events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record LocationEvent(
        @JsonProperty("event_id") String eventId,
        @JsonProperty("event_type") String eventType,
        @JsonProperty("entity_type") String entityType,
        @JsonProperty("entity_id") String entityId,
        @JsonProperty("occurred_at") Instant occurredAt,
        @JsonProperty("location_id") String locationId,
        @JsonProperty("previous_location_id") String previousLocationId,
        @JsonProperty("new_location_id") String newLocationId,
        @JsonProperty("location_name") String locationName,
        String country,
        Double latitude,
        Double longitude
) {
}
