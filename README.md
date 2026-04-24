# Smart Campus API – RESTful Service (JAX-RS)

## Overview

This project implements a RESTful API for managing a Smart Campus system. It allows managing **Rooms**, **Sensors**, and **Sensor Readings** using JAX-RS (Jersey).

The system is designed following RESTful principles:

* Resource-based architecture
* Proper HTTP methods and status codes
* Nested resource hierarchy
* Structured JSON responses
* Robust error handling and logging

---

## Technology Stack

* Java
* JAX-RS (Jersey)
* Maven
* Apache Tomcat (Deployment)
* In-memory data structures (HashMap, ArrayList)

---

## How to Run the Project

### Option 1: Using Maven (Recommended)

1. Open terminal in project root
2. Build project:

```bash
mvn clean install
```

3. Locate generated WAR file:

```text
target/SmartCampusAPI-1.0-SNAPSHOT.war
```

4. Copy WAR into Tomcat:

```text
apache-tomcat/webapps/
```

5. Start Tomcat server

---

### Option 2: Using NetBeans

1. Open project in NetBeans
2. Right-click → **Clean and Build**
3. Locate WAR file in:

```text
target/SmartCampusAPI-1.0-SNAPSHOT.war
```

4. Copy to:

```text
apache-tomcat/webapps/
```

5. Start Tomcat

---

## Base URL

```text
http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1
```

---

## API Endpoints

### Discovery Endpoint

```http
GET /api/v1
```

Returns API metadata and available resources.

---

### Room Management

```http
GET    /rooms
POST   /rooms
GET    /rooms/{id}
DELETE /rooms/{id}
```

---

### Sensor Management

```http
POST /sensors
GET  /sensors
GET  /sensors?type=CO2
```

---

### Sensor Readings (Sub-Resource)

```http
GET  /sensors/{id}/readings
POST /sensors/{id}/readings
```

---

## Error Handling

| Scenario              | Exception                       | HTTP Status               |
| --------------------- | ------------------------------- | ------------------------- |
| Room has sensors      | RoomNotEmptyException           | 409 Conflict              |
| Invalid roomId        | LinkedResourceNotFoundException | 422 Unprocessable Entity  |
| Sensor in maintenance | SensorUnavailableException      | 403 Forbidden             |
| Unexpected error      | GlobalExceptionMapper           | 500 Internal Server Error |

All errors return structured JSON responses.

---

## Logging

Logging is implemented using:

* ContainerRequestFilter
* ContainerResponseFilter

Logs include:

* HTTP Method
* Request URI
* Response status code

---

## Sample cURL Commands

### Create Room

```bash
curl -X POST http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"LIB-301","name":"Library","capacity":100}'
```

---

### Get Rooms

```bash
curl http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1/rooms
```

---

### Create Sensor

```bash
curl -X POST http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-1","type":"Temperature","status":"ACTIVE","roomId":"LIB-301"}'
```

---

### Filter Sensors

```bash
curl http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1/sensors?type=Temperature
```

---

### Add Sensor Reading

```bash
curl -X POST http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1/sensors/TEMP-1/readings \
-H "Content-Type: application/json" \
-d '{"value":23.5}'
```

---

## Conceptual Report

### Part 1: Service Architecture
**Q: Explain the default lifecycle of a JAX-RS Resource class. How does this decision impact data synchronization?**
By default, JAX-RS resource classes are request-scoped, meaning a new instance is created for every incoming request. Because instances are not shared, managing in-memory data requires using `static` collections (like `HashMap`) to persist data across different requests. This necessitates thread-safe practices, such as using `ConcurrentHashMap` or synchronized blocks, to prevent race conditions when multiple clients access or modify the data simultaneously.

**Q: Why is HATEOAS considered a hallmark of advanced RESTful design?**
HATEOAS (Hypermedia as the Engine of Application State) allows a client to interact with the API entirely through responses provided dynamically by the server. This decouples the client from hardcoded URLs, making the API more flexible and self-documenting. Client developers benefit because they can discover available actions and navigate the system without relying solely on external, static documentation.

### Part 2: Room Management
**Q: What are the implications of returning only IDs versus full objects in a list?**
Returning only IDs reduces network bandwidth and speeds up the initial response, which is beneficial for mobile or low-latency environments. However, it requires the client to make subsequent requests (round-trips) to fetch details for each ID. Returning full objects provides all data at once but increases the payload size, which can degrade performance if the list is very large.

**Q: Is the DELETE operation idempotent in your implementation?**
Yes, the implementation is idempotent. If a client sends the same DELETE request multiple times, the first request will successfully remove the room. Subsequent requests will find that the resource no longer exists and return a `404 Not Found`. While the status codes differ, the "state" of the server remains the same (the room is gone), satisfying the definition of idempotency.

### Part 3: Sensor Operations
**Q: Explain the consequences if a client sends data in a different format than JSON.**
Because of the `@Consumes(MediaType.APPLICATION_JSON)` annotation, JAX-RS will reject requests with incompatible `Content-Type` headers (e.g., `text/plain`). The runtime will automatically return an `HTTP 415 Unsupported Media Type` error, ensuring the backend logic never attempts to process unparseable data formats.

**Q: Why is the query parameter approach superior for filtering collections?**
Query parameters are intended for non-hierarchical data adjustments like filtering, sorting, or searching. Using the URL path for filters (e.g., `/sensors/type/CO2`) implies a rigid resource hierarchy that doesn't exist, whereas `?type=CO2` clearly communicates that the client is looking at the same collection but with a specific view or constraint applied.

### Part 4: Sub-Resources
**Q: Discuss the architectural benefits of the Sub-Resource Locator pattern.**
This pattern promotes clean separation of concerns. Instead of one "God Class" managing every nested path, logic is delegated to specialized classes (like `SensorReadingResource`). This reduces code complexity, improves maintainability, and makes the API structure more intuitive by mirroring the logical relationship between sensors and their readings.

### Part 5: Error Handling & Logging
**Q: Explain the risks of exposing internal Java stack traces to external consumers.**
Exposing stack traces is a significant security risk as it reveals internal implementation details, such as class names, library versions, and server architecture. Attackers can use this information to identify specific vulnerabilities in the software stack or gain insights into the database structure to craft more targeted attacks.

**Q: Why use JAX-RS filters for cross-cutting concerns like logging?**
Filters provide a centralized way to handle cross-cutting concerns without duplicating code. Instead of manually inserting logging statements into every resource method, a single filter intercepts all requests and responses. This ensures consistency, makes the code cleaner, and allows developers to enable or disable logging globally with minimal effort.
