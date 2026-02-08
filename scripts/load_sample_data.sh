#!/usr/bin/env bash
set -euo pipefail

bash scripts/curl_index.sh >/dev/null
echo "Loaded sample document"
