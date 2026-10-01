from functools import lru_cache

from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    elasticsearch_url: str = "http://localhost:9200"
    elasticsearch_username: str | None = None
    elasticsearch_password: str | None = None
    elasticsearch_analysis_index: str = "car-rental-location-analyses"
    elasticsearch_events_index: str = "car-rental-location-events"
    kafka_bootstrap_servers: str = "localhost:9092"
    kafka_location_events_topic: str = "car-rental.location-events"
    kafka_consumer_group: str = "car-rental-location-eda"
    kafka_enabled: bool = True
    analysis_requested_by: str = "car-rental-eda@example.com"
    api_host: str = "0.0.0.0"
    api_port: int = 8060

    model_config = SettingsConfigDict(env_file=".env", env_file_encoding="utf-8", extra="ignore")


@lru_cache
def get_settings() -> Settings:
    return Settings()
