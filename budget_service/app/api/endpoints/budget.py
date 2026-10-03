import asyncio
from fastapi import APIRouter, Depends
from app.core.config import get_settings, Settings
from app.services.elasticsearch import create_elasticsearch_client
from elasticsearch import Elasticsearch

router = APIRouter()

@router.get("/history")
async def get_budget_history(
    email: str,
    start_date: str,
    end_date: str,
    page: int = 1,
    size: int = 10,
    settings: Settings = Depends(get_settings),
    es_client: Elasticsearch = Depends(create_elasticsearch_client)
):
    query = {
        "bool": {
            "must": [
                {"term": {"email": email}},
                {"range": {"date": {"gte": start_date, "lte": end_date}}}
            ]
        }
    }
    
    res = await asyncio.to_thread(
        es_client.search,
        index=settings.budget_history_index,
        query=query,
        from_=(page - 1) * size,
        size=size
    )
    
    return res['hits']['hits']