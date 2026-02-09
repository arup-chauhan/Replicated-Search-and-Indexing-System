#!/usr/bin/env bash
set -euo pipefail

MONITORING_PROJECT_NAME="${MONITORING_PROJECT_NAME:-replicated-search-and-indexing-system-monitoring}"

# Go to monitoring folder.
cd "$(dirname "$0")/../monitoring_microservice"

# Substitute env vars into prometheus.yml.template.
envsubst < prometheus.yml.template > prometheus.yml

# Start monitoring stack with root .env values.
docker compose -p "${MONITORING_PROJECT_NAME}" --env-file ../.env up -d
