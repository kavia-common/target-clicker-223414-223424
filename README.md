# Click Quest Backend

This backend powers the Click Quest frontend. It exposes simple REST endpoints under `/api` to manage health checks, score submission, and leaderboard retrieval.

## Environment

Set the following environment variables before running:

- APP_PORT: Server port (default: 3001)
- APP_CORS_ALLOWED_ORIGINS: Comma-separated list of origins allowed by CORS.
  - For local development with the React app on port 3000, set:
    APP_CORS_ALLOWED_ORIGINS=http://localhost:3000

Example `.env`:
```
APP_PORT=3001
APP_CORS_ALLOWED_ORIGINS=http://localhost:3000
```

## API Endpoints

- GET /api/health
  - Purpose: Liveness/readiness check.
  - Response: 200 OK with simple JSON (e.g., {"status":"ok"}).

- GET /api/leaderboard?limit=10
  - Purpose: Retrieve top scores.
  - Query:
    - limit (optional): number of items to return (default and max depend on implementation).
  - Response: 200 OK with an array of scores:
    [
      { "name":"Alice","score":42,"durationMs":30000,"createdAt":"2025-01-01T00:00:00Z" },
      ...
    ]

- POST /api/scores
  - Purpose: Submit a new score.
  - Body (JSON):
    { "name":"string (1-20 chars)", "score":number, "durationMs":number }
  - Response: 201 Created (or 200 OK) with created resource or confirmation.

## Local Development

1) Ensure env is set appropriately (see above).
2) Start the backend on port 3001.
3) Verify health endpoint at:
   http://localhost:3001/api/health

With the backend running, start the React frontend on port 3000 and ensure CORS allows http://localhost:3000.

## Notes

- Do not hardcode origins. Always use APP_CORS_ALLOWED_ORIGINS.
- Keep endpoints scoped under /api to align with the frontend’s expectations.
