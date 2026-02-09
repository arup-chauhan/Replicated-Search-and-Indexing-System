#!/usr/bin/env bash
set -euo pipefail

APP_PROJECT_NAME="${APP_PROJECT_NAME:-replicated-search-and-indexing-system-app}"
MONITORING_PROJECT_NAME="${MONITORING_PROJECT_NAME:-replicated-search-and-indexing-system-monitoring}"

docker compose -p "${APP_PROJECT_NAME}" --env-file .env down
docker compose -p "${MONITORING_PROJECT_NAME}" -f monitoring_microservice/docker-compose.yml --env-file .env down
