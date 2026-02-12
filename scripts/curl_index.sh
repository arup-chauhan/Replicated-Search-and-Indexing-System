#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
USERNAME="${USERNAME:-arup}"
PASSWORD="${PASSWORD:-changeme}"

# Register may fail if user already exists; continue to login.
curl -s -X POST "${BASE_URL}/api/auth/register?username=${USERNAME}&password=${PASSWORD}" >/dev/null || true

TOKEN=$(curl -s -X POST "${BASE_URL}/api/auth/login?username=${USERNAME}&password=${PASSWORD}" | jq -r '.token')

if [[ -z "${TOKEN}" || "${TOKEN}" == "null" ]]; then
  echo "Failed to obtain JWT token"
  exit 1
fi

curl -s -X POST "${BASE_URL}/api/index" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Doc One","content":"Hello Lucene world","tags":["hello","lucene"]}' | jq
