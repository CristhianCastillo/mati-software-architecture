# Software Architecture Project

## Prerequisites

- Java 21 JDK
- Gradle 8.10.3
- Docker & Docker Compose

## Quick Start

### 1. Start Observability Stack

```bash
cd reto-1/observability
docker compose up -d
```

### 2. Build and Run Matching Engine

```bash
cd reto-1/matching-engine-ms
./gradlew clean build
cd applications/app-service/build/libs
java -jar matching-engine-ms.jar
```

### 3. Run Load Tests

```bash
cd reto-1/load-test
./load-test.sh
```

## Monitoring

Access Prometheus metrics at: http://localhost:9090/query?g0.expr=order_matching_time_seconds_max&g0.tab=graph

## Infrastructure Management

| Action | Command |
|--------|---------|
| Start | `docker compose start` |
| Stop | `docker compose stop` |
| Destroy | `docker compose down` |

*Run commands from `reto-1/observability` directory*