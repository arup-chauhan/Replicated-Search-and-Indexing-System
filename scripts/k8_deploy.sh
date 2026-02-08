#!/usr/bin/env bash
set -euo pipefail

if ! command -v kubectl >/dev/null 2>&1; then
  echo "kubectl is required"
  exit 1
fi

if ! command -v minikube >/dev/null 2>&1; then
  echo "minikube is required"
  exit 1
fi

if ! command -v envsubst >/dev/null 2>&1; then
  echo "envsubst is required (install gettext)"
  exit 1
fi

if [[ ! -f .env ]]; then
  echo ".env file not found"
  exit 1
fi

set -a
source .env
set +a

# Resolve cluster-internal service endpoints regardless of local .env defaults.
POSTGRES_URL="jdbc:postgresql://postgres:${POSTGRES_PORT}/engine"
REDIS_HOST="redis"

# Build app image directly into Minikube Docker daemon.
eval "$(minikube docker-env)"
docker build -t "${ENGINE_IMAGE}" .

# Ensure ingress controller exists for Ingress resource.
minikube addons enable ingress >/dev/null

for f in k8/ConfigMap.yaml k8/Secret.yaml k8/Postgresk8.yaml k8/Redisk8.yaml k8/Deployment.yaml k8/Service.yaml k8/Ingress.yaml; do
  echo "Applying $f..."
  envsubst < "$f" | kubectl apply -f -
done

echo "Waiting for deployments..."
kubectl rollout status deployment/postgres --timeout=180s
kubectl rollout status deployment/redis --timeout=180s
kubectl rollout status deployment/replicated-search-indexing-system --timeout=180s

echo "Done. Add this to /etc/hosts if needed:"
minikube ip | awk -v host="${ENGINE_HOST}" '{print $1 " " host}'
