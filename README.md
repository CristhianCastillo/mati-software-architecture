# Software Architecture Project

## Prerequisites

- Java 21 JDK
- Gradle 8.10.3
- Docker & Docker Compose

## Quick Start

### 1. Start Observability Stack

**Linux/macOS:**
```bash
cd reto-1/observability
docker compose up -d or docker-compose up -d
```

**Windows (Command Prompt):**
```cmd
cd reto-1\observability
docker-compose up -d
```

**Windows (PowerShell):**
```powershell
cd reto-1/observability
docker-compose up -d
```

### 2. Build and Run Matching Engine

**Linux/macOS:**
```bash
cd reto-1/matching-engine-ms
./gradlew clean build
cd applications/app-service/build/libs
java -jar matching-engine-ms.jar
```

**Windows (Command Prompt):**
```cmd
cd reto-1\matching-engine-ms
gradlew.bat clean build
cd applications\app-service\build\libs
java -jar matching-engine-ms.jar
```

**Windows (PowerShell):**
```powershell
cd reto-1/matching-engine-ms
./gradlew.bat clean build
cd applications/app-service/build/libs
java -jar matching-engine-ms.jar
```

### 3. Run Load Tests

**Linux/macOS:**
```bash
cd reto-1/load-test
./load-test.sh
```

**Windows (Command Prompt/PowerShell):**
```cmd
cd reto-1\load-test
load-test.bat
```
*Note: If load-test.bat doesn't exist, create it or run the equivalent commands manually*

## Monitoring

Access Prometheus metrics at: http://localhost:9090/query?g0.expr=order_matching_time_seconds_max&g0.tab=graph

## Infrastructure Management

**Linux/macOS:**
| Action | Command |
|--------|---------|
| Start | `docker compose start` |
| Stop | `docker compose stop` |
| Destroy | `docker compose down` |

**Windows (Command Prompt):**
| Action | Command |
|--------|---------|
| Start | `docker compose start` |
| Stop | `docker compose stop` |
| Destroy | `docker compose down` |

**Windows (PowerShell):**
| Action | Command |
|--------|---------|
| Start | `docker compose start` |
| Stop | `docker compose stop` |
| Destroy | `docker compose down` |

*Run commands from `reto-1/observability` directory*