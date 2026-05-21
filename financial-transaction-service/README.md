# Financial Transaction Processing Service

This is an advanced Spring Boot backend service simulating a simplified financial transaction system (payment gateway/banking transaction processor).

## Core Features
*   **Account Management**: Create and fetch accounts with balance tracking.
*   **Transactions**: Handle Debit, Credit, and Transfer operations.
*   **Idempotency**: Prevent duplicate transactions using `Idempotency-Key` headers.
*   **Optimistic Locking**: Handle concurrent transaction modifications safely using Hibernate `@Version`.
*   **Fraud Detection**: Validate transaction limits based on configurable rules.
*   **Audit Logging**: Every successful transaction is logged into an audit table.
*   **Global Exception Handling**: Returns clean JSON errors for validations and business logic issues.

## Tech Stack
*   Java 17
*   Spring Boot 3.2.5 (Web, Data JPA, Validation, Actuator)
*   MySQL Database
*   Lombok
*   Swagger / OpenAPI

## Setup Instructions

### 1. Database Setup
Ensure you have MySQL installed and running on `localhost:3306`.
Create a database named `financial_service`:
```sql
CREATE DATABASE financial_service;
```

### 2. Import into Eclipse
1. Open Eclipse.
2. Go to `File` -> `Import` -> `Maven` -> `Existing Maven Projects`.
3. Select this directory and click `Finish`.

### 3. Running the Application
Right-click on `FinancialTransactionServiceApplication.java` -> `Run As` -> `Java Application` (or `Spring Boot App`).

The application will automatically:
1. Create the database tables (`schema-update`).
2. Insert default fraud rules and test accounts using `data.sql`.

### 4. API Documentation
Once the application is running, you can access the Swagger UI at:
*   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
*   [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

### 5. Testing with Postman
Import the provided `Financial_Transaction_Service.postman_collection.json` into Postman to test the APIs quickly.
