.PHONY: help up down test clean run

.DEFAULT_GOAL := help

help:
	@echo "Available commands:"
	@echo "  up     - Start the app with Docker"
	@echo "  down   - Stop the app"
	@echo "  run    - Run Spring Boot locally"
	@echo "  test   - Run JUnit tests with Maven"
	@echo "  clean  - Clean everything"
	@echo "  help   - Show this help message"

up:
	docker-compose up -d

down:
	docker-compose down

run:
	mvn spring-boot:run

test:
	mvn test

clean:
	mvn clean
	rm -rf target
