# Backend Service

This Spring Boot service starts on port 3001.

Verification checklist:
- Health: http://localhost:3001/health should return "OK"
- Docs redirect: http://localhost:3001/docs should redirect to Swagger UI at /swagger-ui.html
- OpenAPI JSON: http://localhost:3001/api-docs
- H2 console: http://localhost:3001/h2-console (JDBC URL: jdbc:h2:mem:demo_db;MODE=LEGACY;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE, username: sa, empty password)

Notes:
- Hibernate dialect is auto-detected; do not set spring.jpa.database-platform for H2.
- spring.jpa.hibernate.ddl-auto=update persists schema for development convenience.
- DB_CLOSE_DELAY=-1 avoids "Database is already closed" errors during startup or context refresh.
