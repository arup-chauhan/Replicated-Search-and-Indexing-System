#!/usr/bin/env bash
set -euo pipefail

docker compose --env-file .env up -d
