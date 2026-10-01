from functools import lru_cache

from pydantic import AliasChoices, Field
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    elasticsearch_url: str = "http://localhost:9200"
    elasticsearch_username: str = "elastic"
    elasticsearch_password: str | None = Field(
        default=None,
        validation_alias=AliasChoices("ELASTICSEARCH_PASSWORD", "ELASTIC_PASSWORD"),
    )
    elasticsearch_analysis_index: str = "car-rental-location-analyses"
    elasticsearch_events_index: str = "car-rental-location-events"
    kafka_bootstrap_servers: str = "localhost:9194"
    kafka_location_events_topic: str = "car-rental.location-events"
    kafka_consumer_group: str = "car-rental-location-eda"
    kafka_enabled: bool = True
    api_host: str = "0.0.0.0"
    api_port: int = 8060

    model_config = SettingsConfigDict(
        env_file=("../.env", ".env"),
        env_file_encoding="utf-8",
        extra="ignore",
    )


@lru_cache
def get_settings() -> Settings:
    return Settings()
