#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
USERNAME="${USERNAME:-arup}"
PASSWORD="${PASSWORD:-pass123}"
QUERY="${1:-hello}"

TOKEN=$(curl -s -X POST "${BASE_URL}/api/auth/login?username=${USERNAME}&password=${PASSWORD}" | jq -r '.token')

if [[ -z "${TOKEN}" || "${TOKEN}" == "null" ]]; then
  echo "Failed to obtain JWT token"
  exit 1
fi

curl -s "${BASE_URL}/api/search?q=${QUERY}&size=10&offset=0" \
  -H "Authorization: Bearer ${TOKEN}" | jq
