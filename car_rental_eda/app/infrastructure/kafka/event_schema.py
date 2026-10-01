from datetime import datetime
from typing import Literal

from pydantic import BaseModel, ConfigDict, EmailStr, Field


class LocationEvent(BaseModel):
    """Event contract published by the Spring service to the EDA topic."""

    model_config = ConfigDict(extra="allow")

    event_id: str
    event_type: str
    entity_type: Literal["location", "client", "supplier", "repairer", "address", "buying"]
    entity_id: str
    occurred_at: datetime
    requested_by: EmailStr | None = None
    location_id: str | None = None
    previous_location_id: str | None = None
    new_location_id: str | None = None
    location_name: str | None = None
    country: str | None = None
    latitude: float | None = Field(default=None, ge=-90, le=90)
    longitude: float | None = Field(default=None, ge=-180, le=180)

    def movement_destination(self) -> str | None:
        return self.new_location_id or self.location_id
