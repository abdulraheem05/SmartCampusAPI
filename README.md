# Smart Campus REST API (JAX-RS)

## 📌 Overview

This project implements a RESTful API for managing a Smart Campus system. The API allows management of:

* Rooms
* Sensors within rooms
* Sensor readings (historical data)

The system is built using **JAX-RS (Jersey)** and follows REST architectural principles including proper HTTP methods, resource hierarchy, and status codes.

---

## ⚙️ How to Run

### Prerequisites

* Java JDK 8+
* Maven
* NetBeans (recommended)

### Steps

1. Clone repository:

```
git clone https://github.com/abdulraheem05/SmartCampusAPI.git
```

2. Open project in NetBeans

3. Run main class:

```
SmartCampusAPI.java
```

4. Server starts at:

```
http://localhost:8080/api/v1
```

---

## 🌐 API Endpoints

### 🔹 Discovery

```
GET /api/v1
```

---

### 🔹 Rooms

| Method | Endpoint    | Description       |
| ------ | ----------- | ----------------- |
| GET    | /rooms      | Get all rooms     |
| POST   | /rooms      | Create room       |
| GET    | /rooms/{id} | Get specific room |
| DELETE | /rooms/{id} | Delete room       |

---

### 🔹 Sensors

| Method | Endpoint          | Description     |
| ------ | ----------------- | --------------- |
| GET    | /sensors          | Get all sensors |
| GET    | /sensors?type=CO2 | Filter sensors  |
| POST   | /sensors          | Create sensor   |

---

### 🔹 Sensor Readings

| Method | Endpoint               | Description  |
| ------ | ---------------------- | ------------ |
| GET    | /sensors/{id}/readings | Get readings |
| POST   | /sensors/{id}/readings | Add reading  |

---

## 🧪 Sample CURL Commands

### Create Room

```
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"R1","name":"Lab","capacity":50}'
```

---

### Get Rooms

```
curl http://localhost:8080/api/v1/rooms
```

---

### Create Sensor

```
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"S1","type":"CO2","status":"ACTIVE","currentValue":0,"roomId":"R1"}'
```

---

### Filter Sensors

```
curl http://localhost:8080/api/v1/sensors?type=CO2
```

---

### Add Reading

```
curl -X POST http://localhost:8080/api/v1/sensors/S1/readings \
-H "Content-Type: application/json" \
-d '{"id":"RD1","timestamp":1710000000,"value":25.5}'
```

---

### Get Readings

```
curl http://localhost:8080/api/v1/sensors/S1/readings
```

---

### Error Case (Invalid Room)

```
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"S2","type":"CO2","status":"ACTIVE","roomId":"INVALID"}'
```

---

## 📘 REPORT ANSWERS

---

### ✅ Part 1

**Q: JAX-RS lifecycle?**

By default, JAX-RS resource classes are instantiated **per request**. This means a new instance is created for each incoming HTTP request. This avoids shared state issues but requires careful management of shared data structures like HashMaps using static storage or proper synchronization to prevent race conditions.

---

**Q: Why HATEOAS?**

HATEOAS allows APIs to provide navigation links dynamically within responses. This reduces dependency on external documentation and enables clients to discover endpoints at runtime, improving flexibility and maintainability.

---

### ✅ Part 2

**Q: IDs vs full objects?**

Returning only IDs reduces bandwidth usage and improves performance, but requires additional client calls. Returning full objects simplifies client-side processing but increases payload size. A balance depends on use case.

---

**Q: Is DELETE idempotent?**

Yes. Repeated DELETE requests produce the same result: the resource remains deleted. Even if the resource no longer exists, the system state does not change further.

---

### ✅ Part 3

**Q: @Consumes mismatch?**

If a client sends data in an unsupported format (e.g., XML instead of JSON), JAX-RS returns **HTTP 415 Unsupported Media Type** automatically.

---

**Q: QueryParam vs PathParam?**

Query parameters are better for filtering because they are optional and flexible. Path parameters imply a fixed hierarchical structure, which is not suitable for dynamic filtering.

---

### ✅ Part 4

**Q: Sub-resource locator benefits?**

It improves modularity and separation of concerns. Instead of one large controller, logic is split into smaller classes, making the API easier to maintain and scale.

---

### ✅ Part 5

**Q: Why HTTP 422 instead of 404?**

422 is more accurate because the request is syntactically correct but semantically invalid (invalid reference inside payload). 404 implies the endpoint itself is missing.

---

**Q: Stack trace risks?**

Exposing stack traces can reveal internal implementation details such as class names, file structure, and libraries, which attackers can exploit.

---

**Q: Why filters for logging?**

Filters centralize cross-cutting concerns like logging, avoiding repetitive code in every resource method and ensuring consistency.
