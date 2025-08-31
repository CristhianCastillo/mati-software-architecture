# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**

## API REST Endpoints

La aplicación expone los siguientes endpoints REST a través del módulo reactive-web:

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

### Ejemplos con cURL

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
