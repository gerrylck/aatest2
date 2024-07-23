# aatest2

This project processes transaction records, aggregates data, and generates CSV output through a REST API and a client application.

## Preparing the Package

Before starting the REST API server or the client application, you need to package the application using Maven. Follow these steps:

1. Open a terminal window.
2. Navigate to the project root directory.
3. Run `mvn clean package` to compile the project and package it into a JAR file. This command also runs any tests included in the project.
4. Once the process completes, you'll find the packaged JAR file in the `target` directory.
## Starting the REST API Server

1. Navigate to the project root directory.
2. Run `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080` to start the `TransactionApplication` server on port 8080.

## Starting the Client Application

1. Open a new terminal window.
2. Navigate to the project root directory.
3. Run `java -jar target/aatest2-1.0-SNAPSHOT-shaded.jar <input-file-path> <output-file-path>` to start the `TransactionClientApp`, replacing `<input-file-path>` and `<output-file-path>` with your specific file paths.