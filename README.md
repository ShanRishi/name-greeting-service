# name-greeting-service

Simple HTTP API built with **Java 21 + Spring Boot 3.5.x + Maven**.

Single endpoint:

```
GET /hello-world?name=alice
```

| Input | Response |
|---|---|
| First letter `A–M / a–m` (e.g. `alice`) | `200 OK` + `{ "message": "Hello Alice" }` |
| First letter `N–Z / n–z` (e.g. `zoe`) | `400 Bad Request` + `{ "error": "Invalid Input" }` |
| Missing, empty or blank `name` | `400 Bad Request` + `{ "error": "Invalid Input" }` |

## Prerequisites

- OpenJDK 21 (`java -version` should show `21.x`)
- Apache Maven 3.9+ (`mvn -v`)

## How to run the application

```bash
# from the repo root (branch: develop)
mvn spring-boot:run
```

Or build + run the jar:

```bash
mvn clean package
java -jar target/name-greeting-service-0.0.1-SNAPSHOT.jar
```

The app listens on `http://localhost:8080`.

Try it:

```bash
curl -i "http://localhost:8080/hello-world?name=alice"
# HTTP/1.1 200 ... {"message":"Hello Alice"}

curl -i "http://localhost:8080/hello-world?name=zoe"
# HTTP/1.1 400 ... {"error":"Invalid Input"}

curl -i "http://localhost:8080/hello-world"
# HTTP/1.1 400 ... {"error":"Invalid Input"}

curl -i "http://localhost:8080/hello-world?name="
# HTTP/1.1 400 ... {"error":"Invalid Input"}
```

## How to run the tests

```bash
mvn test
```

Test layers:

- `service/GreetingServiceTest` — pure unit tests (valid, invalid, null/empty/blank, non-letter, M/N boundary, trimming).
- `controller/HelloWorldControllerTest` — `@WebMvcTest` + `MockMvc` slice tests verifying status code, `application/json`, and exact JSON body.
- `HelloWorldApiIntegrationTest` — full `@SpringBootTest(RANDOM_PORT)` boot test with `TestRestTemplate`.

## Project structure

```
src/main/java/com/example/greeting/
├── NameGreetingServiceApplication.java  # Spring Boot entry point
├── controller/HelloWorldController.java # GET /hello-world, thin HTTP layer
├── service/GreetingService.java         # validation + greeting rule (pure, unit-testable)
├── dto/HelloResponse.java               # record { message }
├── dto/ErrorResponse.java               # record { error }
└── exception/
    ├── InvalidInputException.java
    └── GlobalExceptionHandler.java       # consistent {"error":"Invalid Input"} for 400s
src/main/resources/application.properties
src/test/java/com/example/greeting/
├── service/GreetingServiceTest.java
├── controller/HelloWorldControllerTest.java
└── HelloWorldApiIntegrationTest.java
```

Rationale: controller handles HTTP only; all business rules live in `GreetingService` so they can be unit-tested without Spring. `GlobalExceptionHandler` guarantees no stacktrace leak and a stable error contract.

## Assumptions made

1. **Capitalization:** `?name=alice` returns `Hello Alice` (first letter uppercased, rest preserved as sent after trimming). So `aLICE` returns `Hello ALICE`. This matches the example in the task.
2. **Trimming:** leading/trailing whitespace is trimmed before validation, so `?name=%20alice%20` is valid and returns `Hello Alice`.
3. **Only `A–Z` can be valid:** digits, symbols, accented/unicode first chars (e.g. `123`, `!bob`, `élice`) return `400`. Only ASCII `A–M` (case-insensitive) is accepted.
4. **Multi-word names:** only the first character matters, so `?name=alice%20bob` returns `Hello Alice bob`.
5. **Method/path handling:** only `GET /hello-world` is supported. Other methods return `405`, unknown paths return `404` (Spring defaults).
6. **No persistence, auth, or rate-limiting** — intentionally out of scope to keep the service simple per the 30–60 min timebox.

## Tech choices

- **Spring Boot 3.3 + Java 21:** production-idiomatic, reviewer-familiar, built-in Jackson + validation + `MockMvc`. Java `record`s used for DTOs.
- **Maven:** as suggested in the assignment.
- **No extra deps:** only `spring-boot-starter-web` + `spring-boot-starter-test` to avoid over-engineering.
