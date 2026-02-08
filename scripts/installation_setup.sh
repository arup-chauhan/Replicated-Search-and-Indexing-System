#!/usr/bin/env bash
set -euo pipefail

echo ">>> Updating package manager"
if command -v brew >/dev/null 2>&1; then
  brew update
else
  sudo apt-get update -y
fi

echo ">>> Installing Java 21 + Maven"
if command -v brew >/dev/null 2>&1; then
  brew install openjdk@21 maven
else
  sudo apt-get install -y openjdk-21-jdk maven
fi

echo ">>> Installing Docker + kubectl + Minikube + Helm"
if command -v brew >/dev/null 2>&1; then
  brew install docker kubectl minikube helm
else
  sudo apt-get install -y docker.io kubectl helm curl
  if ! command -v minikube >/dev/null 2>&1; then
    curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
    sudo install minikube-linux-amd64 /usr/local/bin/minikube
    rm -f minikube-linux-amd64
  fi
fi

echo ">>> Installing Redis and PostgreSQL"
if command -v brew >/dev/null 2>&1; then
  brew install redis postgresql
else
  sudo apt-get install -y redis-server postgresql
fi

echo ">>> Verifying installations"
java -version
mvn -v
docker --version
kubectl version --client
helm version
redis-server --version
psql --version
echo "minikube version:"
minikube version || true

echo ">>> Setup complete for Replicated Search and Indexing System stack"
