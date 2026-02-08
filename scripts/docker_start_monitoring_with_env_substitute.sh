#!/usr/bin/env bash
set -euo pipefail

# Go to monitoring folder.
cd "$(dirname "$0")/../monitoring_microservice"

# Substitute env vars into prometheus.yml.template.
envsubst < prometheus.yml.template > prometheus.yml

# Start monitoring stack with root .env values.
docker compose --env-file ../.env up -d
