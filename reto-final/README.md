# Software Architecture Project :: Reto Final

## Prerequisites

- Java 21 JDK
- Gradle 8.10.3
- Docker & Docker Compose

### Microservices Compilation
- cd reto-final
- sh push_docker_images.sh

### Infraestructure
- cd reto-final/infra
- Init: docker compose up -d
- Refresh Images: docker compose pull
- Refresh all configuration: docker compose up -d --force-recreate
- Start: docker compose start
- Stop: docker compose stop
- Destroy: docker compose down

### Execution Flow
- Process Orders: curl --location 'http://localhost:8081/api/v1/order' \
--header 'Content-Type: application/json' \
--data '{
  "id": "2",
  "productId": "32123",
  "productCount": "342343"   
}'

## Monitoring

Access Prometheus metrics at: http://localhost:3000/d/ad9h4kp/software-architecture-reto-final?orgId=1&refresh=10s

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

*Run commands from `reto-final/observability` directory*

# Sources:
- https://grafana.com/docs/grafana-cloud/send-data/alloy/tutorials/send-logs-to-loki/
- https://grafana.com/docs/loki/latest/get-started/quick-start/quick-start/