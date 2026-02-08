# Replicated Search and Indexing System - TO-DO

Last updated: 2026-02-08

## Immediate (P0)

- [ ] Close API contract alignment across `README.md`, `openapi/openapi.yaml`, and curl scripts.
- [ ] Replace placeholder gRPC metadata service with DB-backed implementation.
- [ ] Validate end-to-end local run (`docker compose up`) and script smoke tests.

## Near-Term (P1)

- [ ] Remove unsafe debug logging from JWT flow and enforce structured logs.
- [ ] Add auth + index/search + metadata + suggest integration tests.
- [ ] Make tests reproducible in CI (stable test profile/Testcontainers).

## Scale Readiness (1M docs, p95 < 200ms)

- [ ] Implement batched Lucene commit/refresh policy.
- [ ] Add query guardrails (size cap, timeouts, expensive query constraints).
- [ ] Add Redis hot-query cache TTL strategy.
- [ ] Add repeatable benchmark harness and SLO gate checks.

## Ops and Packaging

- [ ] Complete or remove placeholder Helm templates.
- [ ] Align `.env.example` with required runtime variables.
- [ ] Keep Minikube manifests and deployment script as the free default path.

## Baseline Exit Criteria

- [ ] `mvn test` passes in a clean environment.
- [ ] Runtime docs and scripts match real API behavior.
- [ ] No placeholder services in request path.
- [ ] Benchmark run and SLO report are reproducible.
