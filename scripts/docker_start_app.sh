#!/usr/bin/env bash
set -euo pipefail

APP_PROJECT_NAME="${APP_PROJECT_NAME:-replicated-search-and-indexing-system-app}"

docker compose -p "${APP_PROJECT_NAME}" --env-file .env up -d
