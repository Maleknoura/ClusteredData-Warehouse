# ClusteredData-Warehouse

A Spring Boot application for processing and storing FX deals data with comprehensive validation.

## Overview

This application accepts FX deal details via REST API, validates the data structure, prevents duplicate imports, and persists valid deals to PostgreSQL database. Built as part of a data warehouse solution for Bloomberg FX deals analysis.

## Requirements

- Java 21+
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL (provided via Docker)

## Quick Start

```bash
# Clone repository
git clone https://github.com/Maleknoura/ClusteredData-Warehouse.git
cd clustered-data-warehouse

# Start application with Docker
make up

# Access application
curl http://localhost:8080/api/deals
```

## API Endpoints

### POST /api/v1/deals
Submit a new FX deal.

**Request:**
```json
{
  "dealUniqueId": "DEAL-001",
  "fromCurrencyCode": "USD",
  "toCurrencyCode": "EUR", 
  "dealTimestamp": "2025-07-28T10:00:00Z",
  "dealAmount": 1000.50
}
```


## Features

### Comprehensive Validation System

The application implements a robust validation framework to ensure data integrity:

#### Currency Validation
- **Format Validation**: All currency codes must follow ISO 4217 standard (3 uppercase letters)
- **Existence Verification**: Currencies are validated against a comprehensive list of recognized currency codes
- **Difference Enforcement**: Source and target currencies must be different for each deal
- **Null/Empty Checks**: All currency fields are required and cannot be null or empty

#### Deal Validation
- **Unique ID Enforcement**: Each deal must have a unique identifier to prevent duplicates
- **Amount Validation**: Deal amounts must be positive numbers
- **Timestamp Validation**: All timestamps must be in valid ISO 8601 format
- **Required Fields**: All deal fields are mandatory

#### Error Handling
- **InvalidCurrencyException**: Thrown for format violations or same currency deals
- **UnknownCurrencyException**: Thrown when currency codes are not recognized
- **Comprehensive Error Messages**: Detailed feedback for all validation failures


## Running the Application

### Using Docker
```bash
# Start services
make up

# View logs
docker compose logs -f

# Stop services  
make down
```

## Testing

```bash
# Run all tests
make test

# Run with coverage
mvn clean test jacoco:report
```

## Configuration

Key environment variables:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

## Project Structure

```
src/
├── main/java/
│   ├── controller/                          # REST endpoints
│   ├── dto/                                 # Data transfer objects
│   ├── entity/                              # JPA entities
│   ├── exception/                           # Custom exceptions
│   ├── mapper/                              # Object mappers
│   ├── repository/                          # Data access
│   ├── service/                             # Business logic
│   └── utils/                               # File loading utilities and validation
├── main/resources/
│   ├── application.yml                      # Configuration
└── test/                                    # Unit & integration tests
```

## Makefile Commands

```bash
make help    # Show available commands
make up      # Start with Docker
make down    # Stop containers
make run     # Run locally
make test    # Execute tests
make clean   # Clean build artifacts
```

## Architecture Highlights

- **Service Layer**: Implements comprehensive business logic validation
- **Repository Pattern**: Clean data access abstraction
- **Exception Handling**: Custom exceptions for different validation scenarios
- **Docker Integration**: Complete containerization with PostgreSQL
- **Test Coverage**: Comprehensive unit and integration testing