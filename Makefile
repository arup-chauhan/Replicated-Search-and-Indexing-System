APP=replicated-search-and-indexing-system-0.1.0
APP_PROJECT_NAME?=replicated-search-and-indexing-system-app


.PHONY: build run test docker-up docker-build


build:
mvn -q -DskipTests package


run: build
java -jar target/$(APP).jar


test:
mvn -q test


docker-build:
docker build -t replicated-search-indexing-system:dev .


docker-up:
docker compose -p $(APP_PROJECT_NAME) --env-file .env up --build
