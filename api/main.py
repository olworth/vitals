import logging
from typing import Annotated

import psycopg
from fastapi import Depends, FastAPI, Request
from fastapi.responses import JSONResponse

from db import get_connection

app = FastAPI()

Conn = Annotated[psycopg.Connection, Depends(get_connection)]

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(name)s] %(levelname)s: %(message)s",
)
logger = logging.getLogger(__name__)


@app.exception_handler(psycopg.OperationalError)
async def db_operational_error_handler(request: Request, exc: psycopg.OperationalError):
    logger.error("db operational error", exc_info=exc)
    return JSONResponse(status_code=503, content={"detail": "database unavailable"})


@app.get("/health")
async def health_check(conn: Conn):
    conn.execute("SELECT 1").fetchone()
    return {"status": "ok", "database": "ok"}


@app.get("/bpm")
async def get_latest_bpm(conn: Conn):
    bpm = conn.execute("select * from vitals ORDER BY recorded_at DESC LIMIT 1;")
    return {"bpm":bpm}
