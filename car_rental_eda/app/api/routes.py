from fastapi import APIRouter, HTTPException, Query, Request
from pydantic import EmailStr

from app.api.schemas import LocationAnalysis, LocationAnalysisRunRequest

router = APIRouter()


@router.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok"}


@router.post("/api/analyses", response_model=LocationAnalysis, status_code=201)
def run_analysis(
    request: Request,
    email: EmailStr = Query(...),
    payload: LocationAnalysisRunRequest | None = None,
) -> dict:
    payload = payload or LocationAnalysisRunRequest()
    try:
        return request.app.state.analysis_runs.run(
            requested_by=str(email),
            period_start=payload.period_start,
            period_end=payload.period_end,
            trigger="api",
        )
    except ValueError as exc:
        raise HTTPException(status_code=422, detail=str(exc)) from exc
    except Exception as exc:
        raise HTTPException(status_code=503, detail="Could not calculate or persist analysis") from exc


@router.get("/api/analyses", response_model=list[LocationAnalysis])
def get_analyses(
    request: Request,
    email: EmailStr = Query(...),
    limit: int = Query(default=20, ge=1, le=100),
) -> list[dict]:
    try:
        return request.app.state.analysis_runs.list_for_email(str(email), limit)
    except Exception as exc:
        raise HTTPException(status_code=503, detail="Could not read analyses from Elasticsearch") from exc
