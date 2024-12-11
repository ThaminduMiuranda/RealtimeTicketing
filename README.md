# Real-Time-Event-Ticketing-System-W2051890

Real-Time Event Ticketing System showcasing Producer-Consumer Pattern is designed to manage concurrent tickets releases and retrievals by Vendors and Customers.

## Tech Stack

<div align="left">
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="Java" title="Java"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" alt="Spring Boot" title="Spring Boot"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png" alt="Maven" title="Maven"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183890595-779a7e64-3f43-4634-bad2-eceef4e80268.png" alt="Angular" title="Angular"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183890598-19a0ac2d-e88a-4005-a8df-1ee36782fde1.png" alt="TypeScript" title="TypeScript"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192158954-f88b5814-d510-4564-b285-dff7d6400dad.png" alt="HTML" title="HTML"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192158956-48192682-23d5-4bfc-9dfb-6511ade346bc.png" alt="Sass" title="Sass"/></code>
</div>

<br>
<br>

<details><summary>Table of Contents</summary>

- [Real-Time-Event-Ticketing-System-W2051890](#real-time-event-ticketing-system-w2051890)
  - [Tech Stack](#tech-stack)
  - [Features](#features)
  - [File Structure](#file-structure)
  - [Project Setup](#project-setup)
    - [Prerequisites](#1-prerequisites)
    - [Backend Setup](#2-backend-setup)
    - [Frontend Setup](#3-frontend-setup)
    - [Usage](#usage)
  - [API References](#api-reference)
    - [Retrieve Logs](#1-retrieve-logs)
    - [Retrieve Configuration](#2-retrieve-configuration)
    - [Update Configuration](#3-update-configuration)
    - [Start Simulation](#4-start-simulation)
    - [Retrieve Simulation Status](#5-retrieve-simulation-status)
    - [Stop Simulation](#6-stop-simulation)
    - [Retrieve Ticket Status](#7-retrieve-ticket-status)
  - [Running Test](#running-tests)
    - [Backend Tests](#backend-tests)
    - [Frontend Tests](#frontend-tests)
  - [Additional Resources](#additional-resources)
  - [License](#license)
  - [Acknowledgments](#acknowledgments)

</details>

## Features

1. **Real-Time Ticket Management**:

   - Vendors can release tickets to a shared pool.
   - Customers can retrieve tickets from the pool.

2. **Concurrency and Thread Safety**:

   - Vendors and customers operate as separate threads, simulating real-world concurrent access.
   - Use of semaphores and synchronized blocks to ensure thread-safe operations.

3. **Configuration Management**:

   - Configuration settings can be loaded from or saved to a JSON file.
   - REST endpoints to retrieve and update configuration settings dynamically.

4. **WebSocket Integration**:

   - Real-time communication between the server and clients using WebSocket.
   - STOMP protocol support for message brokering.

5. **Cross-Origin Resource Sharing (CORS)**:

   - Configured to allow secure communication between the Angular frontend and Spring Boot backend.

6. **Logging**:

   - Centralized logging using Log4j2.
   - REST endpoint to retrieve filtered logs for monitoring and debugging.

7. **Simulation Control**:

   - REST endpoints to start, stop, and check the status of the ticketing simulation.
   - CLI interface for configuring and managing the simulation.

8. **RESTful API**:

   - Endpoints for managing tickets, configuration, logs, and simulation.
   - JSON-based responses for easy integration with frontend applications.

9. **Frontend Integration**:

   - Angular frontend to interact with the backend services.
   - CORS configuration to allow frontend access.

10. **Scalability**:

    - Use of ExecutorService for managing vendor and customer threads.
    - Configurable parameters to test different simulation scenarios.

11. **Error Handling**:

    - Graceful handling of errors and exceptions with appropriate logging and responses.

12. **User Input Validation**:
    - Validated input retrieval to ensure consistent and valid data during configuration.

These features collectively provide a robust and scalable real-time event ticketing system with a focus on concurrency, real-time updates, and ease of configuration and monitoring.

## File Structure

```
└── RealtimeTicketing
   │   .gitignore
   │   pom.xml
   │   README.md
   │   README_REF.md
   │   RealTimeTicketing.iml
   │   system.log
   │   system_config.json
   │
   ├───.idea
   ├───docs
   ├───frontend
   │   └───ticketingFrontend
   │       │   .editorconfig
   │       │   .gitignore
   │       │   angular.json
   │       │   package-lock.json
   │       │   package.json
   │       │   README.md
   │       │   tsconfig.app.json
   │       │   tsconfig.json
   │       │   tsconfig.spec.json
   │       │
   │       ├───.angular
   │       ├───node_modules
   │       ├───public
   │       └───src
   ├───logs
   │       application.log
   │
   └───src
      ├───main
      │   ├───java
      │   │   ├───com
      │   │   │   └───W2051890
      │   │   │       └───ticketing_system
      │   │   │           │   Main.java
      │   │   │           │   TicketingSystemCLI.java
      │   │   │           │
      │   │   │           ├───config
      │   │   │           │       WebSocketConfig.java
      │   │   │           │
      │   │   │           ├───controller
      │   │   │           │       LogController.java
      │   │   │           │       SimulationController.java
      │   │   │           │       TicketController.java
      │   │   │           │
      │   │   │           ├───model
      │   │   │           │       Configuration.java
      │   │   │           │       Customer.java
      │   │   │           │       LogEntry.java
      │   │   │           │       TicketPool.java
      │   │   │           │       Vendor.java
      │   │   │           │
      │   │   │           ├───service
      │   │   │           │       InputService.java
      │   │   │           │       LogService.java
      │   │   │           │       TicketService.java
      │   │   │           │
      │   │   │           └───util
      │   │   │                   LoggerUtil.java
      │   │   │
      │   │   ├───org
      │   │   │   └───thamindu
      │   │   │       └───realtimeticketing
      │   │   │           │   RealtimeTicketingApplication.java
      │   │   │           │   TicketingSystemCLI.java
      │   │   │           │
      │   │   │           ├───config
      │   │   │           │       CorsConfig.java
      │   │   │           │       WebSocketConfig.java
      │   │   │           │
      │   │   │           ├───controller
      │   │   │           │       ConfigurationController.java
      │   │   │           │       LogController.java
      │   │   │           │       SimulationController.java
      │   │   │           │       TicketController.java
      │   │   │           │
      │   │   │           ├───model
      │   │   │           │       Configuration.java
      │   │   │           │       Customer.java
      │   │   │           │       TicketPool.java
      │   │   │           │       Vendor.java
      │   │   │           │
      │   │   │           ├───service
      │   │   │           │       InputService.java
      │   │   │           │       SimulationService.java
      │   │   │           │
      │   │   │           └───util
      │   │   │                   LoggerUtil.java
      │   │   │
      │   │   └───resources
      │   │           application.properties
      │   │
      │   └───resources
      │           application.properties
      │           log4j2.xml
      │
      └───test
         └───java
                  LogConfigChecker.java
                  LoggerTest.java

```

## Project Setup

### 1. Prerequisites

**Java Development Kit (JDK)** : `Java 21` <br>
**Node.js** : `Version ^18` </br>
**Angular CLI** : `Version: ^19.0.4` </br>
**Maven** : `Version: ^ 3.8.6`

### 2. Backend Setup

1. Clone the repository:

   ```sh
   git clone https://github.com/ThaminduMiuranda/RealtimeTicketing.git
   ```

2. Navigate to the project directory:

   ```sh
    cd RealtimeTicketing
   ```

3. Edit Run/Debug Configuration in IntelliJ:

   Before running the backend, edit the Run/Debug Configuration in IntelliJ with VM options for both `TicketingSystemCLI.java` and `RealtimeTicketingApplication.java`:

   ```sh
   -Dconfig.file.path=your/absolute/path/to/system_config.json
   ```

4. Add configuration to `application.properties`:

   In `src/main/resources/application.properties`, add:

   ```sh
   -Dconfig.file.path=your/absolute/path/to/system_config.json
   ```

5. Build the backend:

   ```sh
   mvn clean install
   ```

### 3. Frontend Setup

1. Navigate to the ticketingFrontend directory:

   ```sh
   cd frontend/ticketingFrontend
   ```

2. Install dependencies:

   ```sh
   npm install
   ```

## Usage

To run the project, execute following commands

1.  Navigate to the project directory:

    ```sh
    cd RealtimeTicketing
    ```

2.  Run the backend:

    ```sh
    mvn spring-boot:run
    ```

    The backend server will start at `http://localhost:8080`.

3.  Navigate to the ticketingFrontend directory:

    ```sh
    cd frontend/ticketingFrontend
    ```

4.  Run the frontend:

    ```sh
    ng serve
    ```

    The frontend server will start at `http://localhost:4200`.

## API Reference

### 1. Retrieve Logs

```http
GET /api/logs
```

| Description                |
| :------------------------- |
| Retrieves application logs |

### 2. Retrieve Configuration

```http
GET /api/configuration
```

| Description                              |
| :--------------------------------------- |
| Retrieves current configuration settings |

### 3. Update Configuration

```http
POST /api/configuration
```

| Parameter | Type   | Description                                    |
| :-------- | :----- | :--------------------------------------------- |
| `body`    | `JSON` | **Required**. Configuration settings to update |

#### Example Request Body

```json
{
  "totalTickets": 100,
  "ticketReleaseRate": 5,
  "customerRetrievalRate": 3,
  "maxTicketCapacity": 200
}
```

### 4. Start Simulation

```http
POST /api/simulation/start
```

| Description                     |
| :------------------------------ |
| Starts the ticketing simulation |

### 5. Retrieve Simulation Status

```http
GET /api/simulation/status
```

| Description                                    |
| :--------------------------------------------- |
| Retrieves the current status of the simulation |

### 6. Stop Simulation

```http
POST /api/simulation/stop
```

| Description                    |
| :----------------------------- |
| Stops the ticketing simulation |

### 7. Retrieve Ticket Status

```http
GET /api/tickets/status
```

| Description                             |
| :-------------------------------------- |
| Retrieves the current status of tickets |

## Running Tests

### Backend Tests

1. **Navigate to the project root directory:**

   ```sh
   cd RealtimeTicketing
   ```

2. **Run the tests:**

   ```sh
   mvn test
   ```

### Frontend Tests

1. **Navigate to the frontend directory:**

   ```sh
   cd frontend/ticketingFrontend
   ```

2. **Run the unit tests:**

   ```sh
   ng test
   ```

3. **Run the end-to-end tests:**

   ```sh
   ng e2e
   ```

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.

For more information on Spring Boot, visit the [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/).

## License

This project is licensed under the MIT License.

[MIT](https://choosealicense.com/licenses/mit/)

## Acknowledgments

I would like to express my sincere gratitude to the lecturers at the Institute of Information Technology (IIT) for their invaluable guidance and support throughout this project. Special thanks to the Object-Oriented Programming (OOP) module team, whose insights and teaching laid the foundation for this project. Their dedication to fostering a deep understanding of OOP principles and real-world applications has been instrumental in my development. Thank you for inspiring and empowering me to take on this challenge..

---
