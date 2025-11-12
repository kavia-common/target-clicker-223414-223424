# Backend Service

This Spring Boot service starts on port 3001.

Verification checklist:
- Health (Actuator): http://localhost:3001/actuator/health should return status "UP"
- Basic health (controller): http://localhost:3001/health should return "OK"
- API health (controller): http://localhost:3001/api/health should return `{"status":"UP"}`
- Docs redirect: http://localhost:3001/docs should redirect to Swagger UI at /swagger-ui.html
- OpenAPI JSON: http://localhost:3001/api-docs
- H2 console: http://localhost:3001/h2-console  
  - JDBC URL: `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`  
  - username: `sa`  
  - password: (leave empty)

Dependency and validation notes:
- Bean Validation provider is available via `spring-boot-starter-validation` and explicitly `org.hibernate.validator:hibernate-validator` with EL `org.glassfish:jakarta.el`, avoiding `jakarta.validation.NoProviderFoundException` on startup.
- Spring Boot 3.4.x manages compatible versions via the `io.spring.dependency-management` plugin.

H2 configuration notes:
- Hibernate dialect is auto-detected; do not set `spring.jpa.database-platform` for H2.
- `spring.jpa.hibernate.ddl-auto=update` persists schema for development convenience.
- `DB_CLOSE_DELAY=-1` and `DB_CLOSE_ON_EXIT=FALSE` avoid "Database is already closed" during refresh and non-SIGINT shutdowns.
- Open Session in View is disabled (`spring.jpa.open-in-view=false`) to prevent session leaks.

Devtools/hot reload stability:
- `spring.devtools.restart.exclude` excludes build outputs and volatile files to prevent restart loops.
- `spring.devtools.restart.enabled=false`, `spring.devtools.add-properties=false`, and `spring.devtools.livereload.enabled=false` fully disable devtools in preview/CI.
- File watch triggers are minimized; verify no automatic restarts occur after initial boot.
