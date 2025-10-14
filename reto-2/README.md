# Software Architecture Project :: Reto 2

## Prerequisites

- Java 21 JDK
- Gradle 8.10.3
- Docker & Docker Compose

### Microservices Compilation
- cd reto-2
- sh push_docker_images.sh

### Infraestructure
- cd reto-2/infra
- Start: docker compose start
- Stop: docker compose stop
- Destroy: docker compose down


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

*Run commands from `reto-2/observability` directory*

# Sources:
- https://grafana.com/docs/grafana-cloud/send-data/alloy/tutorials/send-logs-to-loki/
- https://grafana.com/docs/loki/latest/get-started/quick-start/quick-start/