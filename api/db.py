import psycopg

from config import settings


def get_connection():
    with psycopg.connect(str(settings.database_url), connect_timeout=5) as conn:
        yield conn
