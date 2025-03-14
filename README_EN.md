# Virtual Thread MVC Framework (VMVC)

A lightweight MVC framework based on Java 21 Virtual Threads with built-in HTTPS and SM (ShangMi) cryptographic algorithm support.

## Features

- High-performance concurrent processing based on Java 21 Virtual Threads
- Built-in HTTPS support with automatic certificate generation
- SM (ShangMi) cryptographic algorithm support via Bouncy Castle
- Clean MVC architecture design
- JSON response support
- Extensible controller system

## Requirements

- JDK 21 or higher
- Maven 3.6 or higher

## Quick Start

### 1. Clone the Project

```bash
git clone [your-repository-url]
cd vmvc
```

### 2. Build the Project

```bash
mvn clean package
```

### 3. Run the Server

```bash
java -jar target/vmvc-1.0-SNAPSHOT.jar
```

The server will start on port 8443 and automatically generate an HTTPS certificate.

### 4. Test the Service

Visit `https://localhost:8443/hello` to test the server.

> Note: Your browser may show a security warning due to the self-signed certificate. This is normal.

## Project Structure

```
src/main/java/com/example/vmvc/
├── Main.java                 # Application entry point
├── server/                   # Server core
│   ├── HttpServer.java      # HTTP/HTTPS server
│   └── ssl/                 # SSL configuration
├── mvc/                     # MVC core components
│   ├── Controller.java      # Controller interface
│   ├── DispatcherServlet.java # Request dispatcher
│   ├── Request.java        # Request wrapper
│   └── Response.java       # Response wrapper
└── example/                # Example code
    └── HelloController.java # Example controller
```

## Development Guide

### Creating a New Controller

1. Implement the `Controller` interface:

```java
public class MyController implements Controller {
    @Override
    public void handle(Request request, Response response) throws IOException {
        // Implement your business logic here
    }
}
```

2. Register the controller:

```java
dispatcherServlet.registerController("/path", new MyController());
```

### HTTPS Configuration

The framework handles HTTPS configuration automatically, including:
- Self-signed certificate generation
- TLS configuration
- Encrypted communication setup

## Security Notes

- Uses TLS 1.3 protocol by default
- PKCS12 format for certificate storage
- Supports SM cryptographic suites
- Production environments should use official SSL certificates

## Performance Optimization

The framework leverages Java 21 Virtual Threads to efficiently handle numerous concurrent connections:
- Each request runs in its own virtual thread
- Low memory footprint
- High concurrency performance

## How to Contribute

Contributions via Pull Requests or Issues are welcome. Before submitting code, please ensure:
1. Code follows the project's coding standards
2. Necessary test cases are added
3. Documentation is updated

## Performance Metrics

The framework achieves high performance through Virtual Threads:
- Capable of handling thousands of concurrent connections
- Minimal resource overhead per connection
- Efficient thread management without traditional thread pool limitations

## API Examples

### Basic JSON Response
```java
public class ExampleController implements Controller {
    @Override
    public void handle(Request request, Response response) throws IOException {
        response.setHeader("Content-Type", "application/json");
        Map<String, String> data = Map.of(
            "message", "Hello, World!",
            "timestamp", String.valueOf(System.currentTimeMillis())
        );
        response.write(new ObjectMapper().writeValueAsString(data));
    }
}
```

### Error Handling
```java
try {
    // Your business logic
} catch (Exception e) {
    response.setStatus(500);
    response.setHeader("Content-Type", "application/json");
    response.write("{\"error\": \"Internal Server Error\"}");
}
```

## License

[MIT License](https://opensource.org/licenses/MIT)

Copyright (c) [Year] [Your Name]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software. 