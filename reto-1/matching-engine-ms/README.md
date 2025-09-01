# Matching Engine Microservice

Microservicio de motor de emparejamiento de órdenes implementado con Clean Architecture, diseñado para gestionar órdenes de compra y venta con funcionalidades de libro de órdenes y cotización en tiempo real.

## Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

### Model
Contiene las entidades del dominio:
- **Order**: Entidad principal que representa una orden con id, tipo (BUY/SELL), precio y cantidad
- **OrderBook**: Libro de órdenes que agrupa órdenes de compra y venta
- **OrderItem**: Elemento individual del libro de órdenes
- **QuotaSummary**: Resumen de cotización con mejor compra y venta
- **Quota**: Información de cotización individual
- **OrderType**: Enumeración para tipos de orden (BUY/SELL)
- **Excepciones**: OrderException, BusinessException, TechnicalException

### Usecase
Implementa la lógica de negocio:
- **OrderUseCase**: Casos de uso para gestión de órdenes (crear, actualizar, cancelar, obtener libro, obtener cotización)

## Infrastructure

### Driven Adapters

#### LMAX Trading Adapter
Implementación del motor de emparejamiento usando LMAX Disruptor:
- **OrderAdapter**: Implementa OrderGateway para gestión de órdenes
- **PriceLevelPQ**: Cola de prioridad para niveles de precio
- **TraceConfig**: Configuración de trazabilidad

### Entry Points

#### Reactive Web
API REST reactiva con Spring WebFlux:
- **MatchEngineController**: Controlador principal con endpoints REST
- **MatchEngineManager**: Gestor de lógica de negocio
- **OrderMapper**: Mapeo entre DTOs y entidades del dominio
- **DTOs**: OrderDTO, OrderBookDTO, QuotaSummaryDTO, QuotaDTO, OrderItemDTO
- **Requests**: CreateOrderRequest, UpdateOrderRequest
- **Configuración**: CORS, Security Headers, Exception Handlers

## Application

Módulo de configuración y arranque de la aplicación con inyección de dependencias automática mediante @ComponentScan.

## API REST Endpoints

### 1. Crear Orden
```
POST /api/v1/order
Content-Type: application/json

{
  "id": 1,
  "orderType": "BUY",
  "price": 100.50,
  "quantity": 10
}
```

### 2. Obtener Libro de Órdenes
```
GET /api/v1/orders
```

### 3. Obtener Resumen de Cotización (Level 1)
```
GET /api/v1/quota/level1
```

### 4. Actualizar Orden
```
PUT /api/v1/order
Content-Type: application/json

{
  "id": 1,
  "price": 105.00,
  "quantity": 15
}
```

### 5. Cancelar Orden
```
DELETE /api/v1/order/{id}
```

## Funcionalidades del Motor de Emparejamiento

- **Gestión de Órdenes**: Crear, actualizar y cancelar órdenes de compra y venta
- **Libro de Órdenes**: Visualización completa del estado actual del mercado
- **Cotización Level 1**: Mejor precio de compra y venta disponible
- **Arquitectura Reactiva**: Implementación no bloqueante con Spring WebFlux
- **Motor LMAX**: Uso de LMAX Disruptor para alto rendimiento

## Ejemplos de Uso

```bash
# Crear orden de compra
curl -X POST http://localhost:8080/api/v1/order \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "orderType": "BUY", "price": 100.50, "quantity": 10}'

# Crear orden de venta
curl -X POST http://localhost:8080/api/v1/order \
  -H "Content-Type: application/json" \
  -d '{"id": 2, "orderType": "SELL", "price": 99.50, "quantity": 5}'

# Obtener libro de órdenes
curl -X GET http://localhost:8080/api/v1/orders

# Obtener mejor compra y venta
curl -X GET http://localhost:8080/api/v1/quota/level1

# Actualizar orden
curl -X PUT http://localhost:8080/api/v1/order \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "price": 105.00, "quantity": 15}'

# Cancelar orden
curl -X DELETE http://localhost:8080/api/v1/order/1
```

## Tecnologías

- **Spring Boot**: Framework base
- **Spring WebFlux**: Programación reactiva
- **LMAX Disruptor**: Motor de alto rendimiento
- **Gradle**: Gestión de dependencias
- **Clean Architecture**: Patrón arquitectónico

## Sources:
- https://red-robot-8317.postman.co/workspace/My-Workspace~d670895d-c3f4-4288-8090-1c0e90e76386/collection/5854488-caed99e8-0d81-4141-97ed-6242927e90cc?action=share&source=copy-link&creator=5854488
- https://www.jetbrains.com/help/idea/class-diagram.html#analyze_graph
- https://lmax-exchange.github.io/disruptor/
- https://martinfowler.com/articles/lmax.html
- https://github.com/backstreetbrogrammer/13_TopCodingInterviewSolutionsInJava?tab=readme-ov-file#problem-11-design-order-matching-engine
- https://github.com/backstreetbrogrammer/13_TopCodingInterviewSolutionsInJava/blob/main/src/test/java/com/backstreetbrogrammer/Q11_MatchingEngine/twoD/OrderBookUsingSortedMapAndPQTest.java