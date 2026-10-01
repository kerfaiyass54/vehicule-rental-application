from elasticsearch import Elasticsearch

from app.infrastructure.config.settings import get_settings


def create_elasticsearch_client() -> Elasticsearch:
    settings = get_settings()
    options: dict[str, str] = {"basic_auth": (settings.elasticsearch_username, settings.elasticsearch_password)} if (
        settings.elasticsearch_username and settings.elasticsearch_password
    ) else {}
    return Elasticsearch(settings.elasticsearch_url, **options)
