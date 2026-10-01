from datetime import datetime
from pydantic import BaseModel, EmailStr


class LocationAnalysisRunRequest(BaseModel):
    period_start: datetime | None = None
    period_end: datetime | None = None


class LocationAnalysis(BaseModel):
    id: str
    requested_by: EmailStr | None = None
    period_start: datetime
    period_end: datetime
    analysis: dict
    created_at: datetime
